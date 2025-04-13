#!/bin/bash

# Check if Gatling is installed
if ! command -v gatling.sh &> /dev/null; then
    echo "Gatling is not installed. Please install Gatling first."
    echo "You can download it from: https://gatling.io/download/"
    exit 1
fi

# Set variables
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULTS_DIR="gatling-results"
SIMULATION_CLASS="com.skyhawk.nba.stress.NBAStatsSimulation"

# Create results directory if it doesn't exist
mkdir -p $RESULTS_DIR

echo "Starting NBA Stats API Gatling Stress Test..."
echo "Simulation: $SIMULATION_CLASS"
echo "Results will be saved to: $RESULTS_DIR"

# Run Gatling test
gatling.sh -sf src/test/scala -rsf src/test/resources -s $SIMULATION_CLASS

echo "Gatling stress test completed!"
echo "Results are available in the Gatling reports directory"

# Find the latest report
LATEST_REPORT=$(find ~/Downloads -name "gatling-*" -type d | sort -r | head -n 1)

if [ -n "$LATEST_REPORT" ]; then
    echo "Latest report: $LATEST_REPORT"
    
    # Open the report in the default browser (macOS)
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open "$LATEST_REPORT/index.html"
    fi
fi 