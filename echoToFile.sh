#!/bin/bash
exec > log.txt
while IFS= read -r line; do
  printf '%s\n' "$line"
done