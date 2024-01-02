import fastavro
import sys
import random
from faker import Faker
from datetime import datetime

fake = Faker()

# One-time setup instructions:
#
# python3 -m venv venv
# source venv/bin/activate
# pip install fastavro faker
#
# For usage, run without any args:
#
# python avro_utils.py
#
# Note: input/output file paths will be relative from the script
#      location, not from the current working directory.  Thus
#      it is recommended to provide fully qualified file paths
#      if running the script from a different location.

"""
    Read the Avro schema from the given Avro file path.
"""
def read_avro_schema(avro_file_path):
    with open(avro_file_path, 'rb') as avro_file:
        return fastavro.reader(avro_file).writer_schema

"""
    Generate the given number of records based on the given schema
    and write them to the given output file path.
"""
def create_fake_avro_file(schema, output_path, num_records=1):
    records = []
    for _ in range(num_records):
        records.append(create_fake_record(schema['fields']))

    with open(output_path, 'w+b') as avro_file:
        fastavro.writer(avro_file, schema, records)


"""
    Generate a fake record based on the given schema fields.
"""
def create_fake_record(fields):
    return {field['name']: fake_value(field) for field in fields}

def fake_value(field):
    field_type = field['type']

    if isinstance(field_type, list):
        # Exclude 'null' when choosing types from the list
        non_null_types = [t for t in field_type if t != 'null']
        if non_null_types:
            chosen_type = random.choice(non_null_types)
            print("Chose type {} for field {}".format(chosen_type, field['name']))
            return fake_value({'name': field['name'], 'type': chosen_type})
        else:
            # All types are 'null', return None
            return None

    if isinstance(field_type, dict):
        if field_type['type'] == 'enum':
            enum_value = random.choice(field_type['symbols'])
            return enum_value

        elif field_type['type'] == 'map':
            return {fake.word(): fake_value({'type': field_type['values']}) for _ in range(random.randint(1, 5))}

        elif field_type['type'] == 'record':
            return create_fake_record(field_type['fields'])

        elif field_type['type'] == 'array':
            return [fake_value({'name': field['name'], 'type': field_type['items']}) for _ in range(random.randint(1, 5))]

    if field_type == 'string':
        # Generate ISO-formatted timestamp string if the name ends with `_datetime`
        if field['name'].endswith('_datetime'):
            return datetime.utcnow().isoformat()
        else:
            return fake.word()
    elif field_type == 'int':
        return fake.random_int()
    elif field_type == 'long':
        return fake.random_int()
    elif field_type == 'float':
        return fake.random_number()
    elif field_type == 'double':
        return fake.random_number()
    elif field_type == 'boolean':
        return fake.boolean()
    elif field_type == 'null':
        return None
    elif field_type == 'bytes':
        return fake.binary(length=random.randint(1, 100))
    elif field_type == 'record':
        return create_fake_record(field_type['fields'])

    else:
        print("Encountered unhandled type in schema: {}".format(field))
        sys.exit(1)
        # Handle other field types as needed
        return None

command_hints = {
    'read_schema': 'read_schema <avro_file_path>',
    'create_empty_avro_file': 'create_empty_avro_file <schema_path> <output_path>',
    'create_fake_avro_file': 'create_fake_avro_file <schema_path> <output_path> <num_records>',
}

def get_command_hint(command):
    return "python avro_utils.py {}".format(command_hints[command])

def fail_with_usage(command):
    print("Usage: {}".format(get_command_hint(command)))
    sys.exit(1)

"""
    Main function.
"""
def main():
    if len(sys.argv) < 3:
        print("Usage:")
        for command, details in command_hints.items():
            print(get_command_hint(command))
        print()
        print("Note: file paths are interpreted relative to the script, so use fully qualified paths if running from a different location.")
        sys.exit(1)

    command = sys.argv[1]
    schema_path = sys.argv[2]

    if command == 'read_schema':
        if len(sys.argv) != 3:
            fail_with_usage(command)
        schema = read_avro_schema(schema_path)
        print("Avro Schema:")
        print(schema)

    elif command == 'create_empty_avro_file':
        if len(sys.argv) != 4:
            fail_with_usage(command)
        output_path = sys.argv[3]
        schema = read_avro_schema(schema_path)
        create_fake_avro_file(schema, output_path, num_records=0)

    elif command == 'create_fake_avro_file':
        if len(sys.argv) != 5:
            fail_with_usage(command)
        output_path = sys.argv[3]
        num_records = int(sys.argv[4])
        schema = read_avro_schema(schema_path)
        create_fake_avro_file(schema, output_path, num_records)

    else:
        print(f"Unknown command: {command}")

if __name__ == "__main__":
    main()
