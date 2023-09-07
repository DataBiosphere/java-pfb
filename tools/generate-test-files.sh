#!/bin/bash
# ASSUMES YOU'VE CHECKED OUT THE pypfb repo and are in the tools directory of java-pfb

set -eu

read -rp "Enter avro file name to generate test files for (Don't include .avro and it should live in the java-pfb/library/src/test/resources/avro directory): " AVRO_FILE_NAME

# generate test files

cd ../../pypfb
pfb show -i ../java-pfb/library/src/test/resources/avro/${AVRO_FILE_NAME}.avro >> ../java-pfb/library/src/test/resources/pyPfbOutput/show/$AVRO_FILE_NAME.json
pfb show -i ../java-pfb/library/src/test/resources/avro/${AVRO_FILE_NAME}.avro metadata >> ../java-pfb/library/src/test/resources/pyPfbOutput/showMetadata/$AVRO_FILE_NAME.json
pfb show -i ../java-pfb/library/src/test/resources/avro/${AVRO_FILE_NAME}.avro nodes >> ../java-pfb/library/src/test/resources/pyPfbOutput/showNodes/$AVRO_FILE_NAME.txt
pfb show -i ../java-pfb/library/src/test/resources/avro/${AVRO_FILE_NAME}.avro schema >> ../java-pfb/library/src/test/resources/pyPfbOutput/showSchema/$AVRO_FILE_NAME.json