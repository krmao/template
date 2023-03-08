#!/bin/bash
# setting up prerequisites

if [ ! -d "/usr/share/elasticsearch/plugins/analysis-ik" ];then
  # bin/elasticsearch-plugin install analysis-icu
  # bin/elasticsearch-plugin install analysis-smartcn
   bin/elasticsearch-plugin install --batch https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v8.6.1/elasticsearch-analysis-ik-8.6.1.zip
else
  echo "analysis-ik already installed"
fi

exec /usr/local/bin/docker-entrypoint.sh elasticsearch
