# NBA Stats API Stress Testing

This directory contains stress testing scripts for the NBA Statistics API. We provide two different approaches:

1. Apache JMeter tests
2. Gatling tests

## Prerequisites

### For JMeter Tests
- Apache JMeter 5.5 or higher
- Java 11 or higher

### For Gatling Tests
- Gatling 3.9 or higher
- Scala 2.13 or higher
- Java 11 or higher

## Running JMeter Tests

1. Make sure your NBA Stats API is running on `localhost:8080`
2. Make the run script executable:
   ```bash
   chmod +x run-stress-test.sh
   ```
3. Run the JMeter test:
   ```bash
   ./run-stress-test.sh
   ```
4. View the results in the generated HTML report

## Running Gatling Tests

1. Make sure your NBA Stats API is running on `localhost:8080`
2. Make the run script executable:
   ```bash
   chmod +x run-gatling-test.sh
   ```
3. Run the Gatling test:
   ```bash
   ./run-gatling-test.sh
   ```
4. View the results in the generated HTML report

## Test Scenarios

Both test suites include the following scenarios:

1. **Player Statistics** - Retrieving statistics for a specific player
2. **Team Statistics** - Retrieving statistics for a specific team
3. **Game Statistics** - Retrieving statistics for a specific game
4. **Game Events** - Retrieving events for a specific game
5. **Leaderboard** - Retrieving leaderboard data for a specific statistic

## Load Patterns

### JMeter Test
- 50 concurrent users
- 10-second ramp-up period
- 5-minute test duration

### Gatling Test
- 50 users per scenario with 10-second ramp-up
- 5 requests per second per scenario for 1 minute
- Assertions for response time and success rate

## Interpreting Results

### Key Metrics to Monitor
- **Response Time**: Average, median, and 95th percentile
- **Throughput**: Requests per second
- **Error Rate**: Percentage of failed requests
- **CPU Usage**: On both client and server
- **Memory Usage**: On both client and server
- **Database Connection Pool**: Utilization and potential bottlenecks

### Performance Criteria
- Response time < 2000ms for 95% of requests
- Error rate < 5%
- No connection timeouts
- Stable CPU and memory usage

## Troubleshooting

If you encounter issues:

1. Check that the API is running and accessible
2. Verify that the database is properly initialized
3. Check for any firewall or network issues
4. Increase the heap size for JMeter/Gatling if needed:
   ```bash
   export JAVA_OPTS="-Xmx4g -Xms2g"
   ```

## Customizing Tests

### JMeter
Edit the `nba-stats-stress-test.jmx` file to modify:
- Number of threads
- Ramp-up period
- Test duration
- Request parameters

### Gatling
Edit the `NBAStatsSimulation.scala` file to modify:
- Injection profiles
- Scenarios
- Assertions
- Request parameters 