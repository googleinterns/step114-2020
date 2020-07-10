#! /bin/sh
pushd edith-step; yarn build; popd;
mvn package appengine:run
