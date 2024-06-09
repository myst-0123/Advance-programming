#!/bin/sh
# imagemagickで何か画像処理をして，/imgprocにかきこみ，テンプレートマッチング
# 最終テストは，直下のforループを次に変更 for image in $1/final/*.ppm; do
for image in $1/test/*.ppm; do
    bname=`basename ${image}`
    name="imgproc/"$bname
    x=0    	#
    echo $name
    convert -resize 50% "${image}" "${name}" # 縮⼩

    rotation=0
    echo $bname:
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
wait
