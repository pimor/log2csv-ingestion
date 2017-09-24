# Log To CSV Ingestion

## Coding challenge scenario:

Your task is to build a data pipeline, which will process data from logs generated by an API platform and load it into a
data warehouse, where the data can be accessed by business analysts using SQL and reporting tools.

#### Data:
The API platform generates JSON-like log files. You can find a compressed sample of the log data at our Google drive:
[Nexmo - Data engineer - Test data](https://drive.google.com/uc?id=0B2hNviBOe8CCeTd4UmtlMXV5YkE&export=download)

Your ETL does not have to pull data from Google drive, assume the provided data will be put uncompressed into the local
filesystem (directory of your choice) by an external process. The data contains about 8 million records and the following
fields:
- message_id
- timestamp
- account_id
- gateway_id
- country
- status
- price
- cost

### Requirements:
Your task is to create a simple periodic ETL process, which will ingest the above data from disk and load it into a
database of your choice, so that the data analysts can use standard (ANSI) SQL to query the data and build reports. As
of now, the business analysts would like to see revenue, gross margin and message volumes, broken down by date, gateway,
country and account. They’d like fresh data to be available daily.

### Task scope:
The purpose of the above task is to give us insight into how you think and code. We don’t expect you to build a complete
solution, but we’d like to see a working, end-to-end minimal product.

Your solution should include:
- ETL code to process and load the above data from disk into a database of your choice
- Code to setup the target database and any other structures used by the ETL process
- Additional code required to launch / schedule the ETL process
- Deployment scripts and any instructions needed to run the process

You don’t need to worry about:
- Loading data from Google drive or decompression of the data
- Any kind of data visualization

As a guidance on the kind of effort we expect, the solution above took us around half a day to build, so we don’t expect
you to spend more than ~8 hours on the task.

### Tips and guidance:
- We value clear, understandable, testable and easy to maintain code above complex solutions
- We use spark, python and redshift, but feel free to use any open source language and framework you’re comfortable with
- Think about the fact that stakeholder requirements may change in the future when designing your solution

## Additional questions:
To give us more insight into your solution, please also answer the following questions:
- How would you change your solution if you had an additional week to work on it? Additional month?

  >**->** With an additional week: Setting up more target systems (i.e. Redshift, MySQL, HDFS, ...) and externalize the
  configuration into a database, so we can rely in this configuration to read the needed information for the ingestion
  process.

  >**->** With an additional month: Refactoring the code to start to build an _ingestion-engine_ to be able to load any
  file into any final system in a easy way and covering things such as traceability, lineage, ingestion statistics, etc.
  Evaluate if it's possible to read the api log directly instead of load a log in a batch mode and feed the table
  continuously.

- Are there any aspects of ETL your solution does not cover that would need to be covered in a production environment?
How would you approach them?

  >**->** Data partitioning: define a partitioning criteria analyzing the data volume (daily, monthly, ...), so the load
  process should take into account this parameter in order to create/load the data in the right partition.
  >**->** Notification system (i.e. alerts, reporting) and process monitoring.

- If your solution needed to process 10 times the data volume provided, how would it change? 100 times?

  >**->** Spark was chosen because the process performing can be increased in a easy way, adjusting the number of
  executors/vcores/memory that are used to perform the operations.

## Checking out and building:
To check out the project and build from source, do the following:

    git clone git@github.com:pimor/log2csv-ingestion.git
    cd log2csv-ingestion
    mvn clean verify

 ## How to run it:

    spark-submit --class builder.LogToCsvIngestion \
    --master local --deploy-mode client --executor-memory 2g \
    --name logToCsv --conf "spark.app.id=log2csv" \
    log2csv-ingestion-0.0.1-SNAPSHOT-jar-with-dependencies.jar ${path-to-apilogs} ${path-to-output-dir}

### Process scheduling
In this first stage, a simple cron entry should be enough to execute the process periodically. The following example
shows how to schedule the script execution each 8 hours (we assume that a ingest_apilog.sh script is created with the
previous spark-submit syntax):

    0 */8 * * * ${path-to-ingest_apilog.sh}

To configure it in your system, just type _crontab -e_ and paste the above line on it (replacing the variable by the
script path).

## Solution details:
It's a Java application that uses Spark to transform the input api log files into a CSV file. The idea is be able to
load the CSV file into **any external system** (H2, RDBMS, Kafka, Redshift, etc.).
It also provides the following features:
- Uses Maven to manage the software lifecycle.
- Uses JUnit to run the localhost tests.
- Uses spark-testing-base to run Spark in localhost mode.

### Implementation details:
Using a regular expression which parses the input api log lines, the app is able to create a CSV file and then it loads
the CSV into a H2 database which is the database used in the built test. The regular expression slices the input
file into the following 11 fields:
- messageId
- date
- hour
- var001
- accountId
- gatewayId
- country
- status
- messagePrice
- transistCost
- routeCost

Converting the 8 input fields into the previous 11 fields allows to write and execute better SQL sentences, especially
when a date grouping is required because we don't need to extract the date from the original timestamp. Finally, the
CSV is loaded into the H2 database.