#!/bin/bash

# Check if JMeter is installed
if ! command -v jmeter &> /dev/null; then
    echo "JMeter is not installed. Please install JMeter first."
    echo "You can download it from: https://jmeter.apache.org/download_jmeter.cgi"
    exit 1
fi

# Set variables
JMETER_HOME=$(which jmeter | xargs dirname | xargs dirname)
TEST_PLAN="nba-stats-stress-test.jmx"
RESULTS_DIR="stress-test-results"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULTS_FILE="$RESULTS_DIR/results_$TIMESTAMP.jtl"
REPORT_DIR="$RESULTS_DIR/report_$TIMESTAMP"

# Create results directory if it doesn't exist
mkdir -p $RESULTS_DIR

echo "Starting NBA Stats API Stress Test..."
echo "Test Plan: $TEST_PLAN"
echo "Results will be saved to: $RESULTS_FILE"
echo "HTML report will be generated in: $REPORT_DIR"

# Run JMeter test
jmeter -n -t $TEST_PLAN -l $RESULTS_FILE -e -o $REPORT_DIR

echo "Stress test completed!"
echo "Results file: $RESULTS_FILE"
echo "HTML report: $REPORT_DIR/index.html"

# Open the report in the default browser (macOS)
if [[ "$OSTYPE" == "darwin"* ]]; then
    open $REPORT_DIR/index.html
fi 