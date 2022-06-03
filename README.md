# My Elastic Quarkus

Personal projects for testing on Quarkus and Elasticsearch dependencies.

In order to run an Elasticsearch instance using podman, run:

```
podman run --name elasticsearch --rm -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \ 
--rm -p 9200:9200 registry.connect.redhat.com/elastic/elasticsearch:latest
```

Then run the application with `quarkus dev`

We can add users like this:
```
curl localhost:9200/users -d '{"name": "luca", "age": "20"}' -H "Content-Type: application/json"
```

And search for users by name or age via the flowing curl command:
```
curl localhost:9200/users/search?age=20
```
