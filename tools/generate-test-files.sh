#!/bin/bash
# Script: java-pfb/tools/generate-test-files.sh
#
# By default: This script scans library/src/test/resources/avro for Avro files and generates the output of
# running various pfb commands on it.  The output is written into library/src/test/resources/pyPfbOutput
#
# An optional single argument can be provided to specify a single Avro file to generate test files for.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTPUT_DIR="$SCRIPT_DIR/../library/src/test/resources/pyPfbOutput"

# Ensure the output directories exist
mkdir -p "$OUTPUT_DIR/show" "$OUTPUT_DIR/showMetadata" "$OUTPUT_DIR/showNodes" "$OUTPUT_DIR/showSchema"

generate_test_files() {
    if [[ ! $1 =~ \.avro$ ]]; then
        echo "Error: $1 does not have .avro extension"
        exit 1
    fi

    # Ensure the specified AVRO file exists
    if [ ! -f "$1" ]; then
        echo "Error: file not found at $1"
        exit 1
    fi

    local avro_filepath
    avro_filepath=$(realpath "$1")

    local output_dir
    output_dir=$(realpath "$OUTPUT_DIR")

    local output_filename
    output_filename=$(basename "$avro_filepath" .avro)

    pfb show -i "$avro_filepath" > "$output_dir/show/$output_filename.json"
    pfb show -i "$avro_filepath" metadata > "$output_dir/showMetadata/$output_filename.json"
    pfb show -i "$avro_filepath" nodes > "$output_dir/showNodes/$output_filename.txt"
    pfb show -i "$avro_filepath" schema > "$output_dir/showSchema/$output_filename.json"

    echo "Generated test files for $avro_filepath in $output_dir"
}

if [ "$#" -eq 1 ]; then
    # If a filename is provided as an argument, generate only that file
    generate_test_files "$1"
else
    AVRO_DIR="$SCRIPT_DIR/../library/src/test/resources/avro"

    # Ensure the AVRO directory exists
    if [ ! -d "$AVRO_DIR" ]; then
        echo "Error: AVRO directory not found at $AVRO_DIR"
        exit 1
    fi
    # Otherwise, generate all files in the AVRO directory
    for avro_file_path in "$AVRO_DIR"/*; do
        generate_test_files "$avro_file_path"
    done
fi
