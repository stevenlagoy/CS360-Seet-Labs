#!/bin/bash

sudo chmod -R 777 ./
sudo npx json-server ./Data/db.json &   
sudo ng serve
