## ga-service
Demo project to show integration with Google Analytics 4 via measurement protocol.


GoogleAnalyticsWorker class:
1) uses Spring Scheduler with cron expression configured in [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
2) get uah/usd ratio from NBU REST API

url configuration 'exchange-rate.uri' in [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)  

3) send GAMP event via REST API (method sendMeasurementProtocolEvent)

POST https://www.google-analytics.com/mp/collect?api_secret={secret}&measurement_id={measurement_id}

Body:
```toml @sample.conf
{
  "clientId": "929090915.1698788043",
  "events": [
    {
      "name": "uah_usd_exchange_rate",
      "params": {
        "rate": 36.1705,
        "debugMode": true,
        "date": '2023-11-05'
      }
    }
  ]
}
```
'clientId' - from '_ga' cookies in https://bank.gov.ua, like in [screenshot](clientId.png)

'api_secret' and 'measurement_id' - from GA account

see in [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)

## Google Analytics report
Exploration - 'Exchange Rate Table' 

and

https://analytics.google.com/analytics/web/#/p414295007/reports/dashboard?params=_u..nav%3Dmaui%26_r..dimension-value%3D%7B%22dimension%22:%22eventName%22,%22value%22:%22uah_usd_exchange_rate%22%7D&r=events-overview&collectionId=life-cycle

Use date range starting from Nov 7, 2023

## Start the project with docker

```bash
$ docker build . -t ga-service
$ docker run ga-service
```

