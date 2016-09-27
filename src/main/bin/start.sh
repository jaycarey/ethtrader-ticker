#!/usr/bin/env bash

cd "$(dirname "$0")/.."

mkdir -p logs

LOG=logs/ticket.$(date "+%Y-%m-%d").$$.log
nohup java -cp .:lib/* org/ethtrader/ticker/EntryKt $* >> $LOG 2>&1 &

tail -F $LOG
