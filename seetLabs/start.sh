#!/bin/bash

chmod -R 777 ./
npx json-server --watch ./Data/db.json &   
ng serve
