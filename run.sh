#!/bin/bash
rm -rf src/main/webapp/src.*
pushd edith-step; yarn build; popd;
mvn package appengine:run
