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
    for i in 0 90 180 270; do
        echo $i%
        for template in $1/*.ppm; do
            tempbname=`basename ${template}`
            tempname="imgproc/"$tempbname
            tempname2="imgproc/"$tempbname+"2"
            convert -rotate $i% "${template}" "${tempname}" # 回転
            convert -resize 50% -rotate $i% "${template}" "${tempname2}" # 回転縮⼩
            echo $tempbname:
	        if [ $x = 0 ]
	        then
	            ./matching $name "${tempname2}" $i 1.0 cpg "${image}" "${tempname}"
	            x=1
	        else
	            ./matching $name "${tempname2}" $i 1.0 pg "${image}" "${tempname}"
	        fi
        done
        echo ""
    done
done
wait

