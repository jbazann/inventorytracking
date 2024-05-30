
WIP DO NOT READ THIS IS A CONCEPT IN DEVELOPMENT


## domain
- Changing or adding new logic will affect the 
corresponding unit test, and potentially any mocked 
instances in unit tests for different classes.
- Changing or adding attributes will have the same
impact as above. It will also affect integration tests,
revise the data builders. The application will need to
be run once in order to generate the dll.sql script,
changes in this script will require the postgres
docker image to be built again. This can be done
manually or by running integration tests, which
always start by updating the image.








