#!/bin/sh

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_100_10100_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_100_10100_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_100_30100_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_100_30100_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_100_90100_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_100_90100_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_200_180200_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_200_180200_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_200_20200_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_200_20200_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_200_60200_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_200_60200_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_50_15050_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_50_15050_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_50_45050_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_50_45050_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_50_5050_0.9_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=tuningCorreto/agm_layer/agm_50_5050_1.0_layer
reference="1.01 1.01"

echo "$system"
FILES=./$system/HYPERVOLUME_N.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"
