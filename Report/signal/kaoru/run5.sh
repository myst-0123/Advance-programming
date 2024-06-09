#!/bin/sh
# imagemagickで何か画像処理をして，/imgprocにかきこみ，テンプレートマッチング
# 最終テストは，直下のforループを次に変更 for image in $1/final/*.ppm; do
# テンプレート50%
for image in $1/test/*.ppm; do
    bname=`basename ${image}`
    name="imgproc/"$bname
    x=0    	#
    echo $name
    convert "${image}" "${name}"  # 何もしない画像処理

    rotation=0

    echo 50%
    for template in $1/*.ppm; do
        tempbname=`basename ${template}`
        tempname="imgproc/"$tempbname
        convert -resize 50% "${template}" "${tempname}" # 縮⼩
        echo $tempbname:
        if [ $x = 0 ]
        then
            ./matching $name "${tempname}" $rotation 0.5 cpg "${image}" "${tempname}"
            x=1
        else
            ./matching $name "${tempname}" $rotation 0.5 pg "${image}" "${tempname}"
        fi
    done
    echo ""
    
done

# 背景画像50%，テンプレート50%
for image in $1/test/*.ppm; do
    bname=`basename ${image}`
    name="imgproc/"$bname
    x=1    	#
    echo 100%
    echo $name
    convert -resize 50% "${image}" "${name}" # 縮⼩
    rotation=0

    for template in $1/*.ppm; do
    tempbname=`basename ${template}`
    tempname="imgproc/"$tempbname
    convert -resize 50% "${template}" "${tempname}" # 縮⼩
	echo $tempbname:
	if [ $x = 0 ]
	then
	    ./matching $name "${tempname}" $rotation 0.3 cpg "${image}" "${template}"
	    x=1
	else
	    ./matching $name "${tempname}" $rotation 0.3 pg "${image}" "${template}"
	fi
    done
    echo ""
done

# 背景画像50%
for image in $1/test/*.ppm; do
    bname=`basename ${image}`
    name="imgproc/"$bname
    x=1    	#
    echo 200%
    echo $name
    convert -resize 50% "${image}" "${name}" # 縮⼩

    rotation=0
    echo $bname:
    for template in $1/*.ppm; do
	tempbname=`basename ${template}`
    tempname="imgproc/"$tempbname
    convert -resize 200% "${template}" "${tempname}" # 拡大
	echo $tempbname:
	if [ $x = 0 ]
	then
	    ./matching $name "${template}" $rotation 0.3 cpg "${image}" "${tempname}"
	    x=1
	else
	    ./matching $name "${template}" $rotation 0.3 pg "${image}" "${tempname}"
	fi
    done
    echo ""
done
wait