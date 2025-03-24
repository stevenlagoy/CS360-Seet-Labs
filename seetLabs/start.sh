#!/bin/bash

chmod -R 777 ./
npx json-server ./Data/db.json &   
ng serve
