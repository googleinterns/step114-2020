#!/bin/bash
<<<<<<< HEAD
=======
mvn clean
>>>>>>> fcf4ae8975a5c332bd4e87a71858910900731524
rm -rf src/main/webapp/src.*
pushd edith-step; yarn build; popd;
mvn package appengine:run
