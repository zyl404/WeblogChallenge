# Command to run the code
```bash
mvn clean package
spark-submit target/yunlei-1.0-SNAPSHOT.jar --class Main
```

# Result
* The result of sessionize will be stored under data/session
* The average session time is printed in the console
```bash
+-----------------+
|   avgSessionTime|
+-----------------+
|7.389610510416582|
+-----------------+

```

* The count of unique URL visits per session is stored together with the session.

* The top 5 engaged users is printed in the console as well
```bash
+--------------------+--------+
|              client|engaging|
+--------------------+--------+
| 54.169.191.85:15462|    3182|
|203.191.34.178:10400|    2649|
|103.29.159.138:57045|    2065|
|213.239.204.204:3...|    2065|
|   78.46.60.71:58504|    2064|
+--------------------+--------+
``` 