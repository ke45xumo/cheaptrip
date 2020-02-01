#!/bin/sh

# Portions of this script uses the following open source software:
# csvquote version commit baf37fa4cccc656282551db4ea7ce4ec6b9c318e, copyrighted by Dan Brown under the terms of the MIT license. The license can be downloaded from: https://github.com/dbro/csvquote/blob/master/LICENSE.md. The source code for this software is available from: https://github.com/dbro/csvquote.

mkdir -p public_html
./csvq public_html/vehicles.csv | cut -d, -f1-4,6-15,17-30,32-34,36-46,49-57,59-63,65-83 --complement | ./csvq -u > temp.csv
sed -i '1d' temp.csv
./csvq temp.csv | sort -k6 -k3 -k4 -k2 -k5 -k1 -t, | ./csvq -u > vehicles_clean.csv
sed -i 1i"cityCo,combinedCo,fuelType,highwayCo,brand,model,transmission,year" vehicles_clean.csv
rm temp.csv
mv vehicles_clean.csv public_html/
