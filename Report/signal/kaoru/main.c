#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "imageUtil.h"
// #include <omp.h>

void templateMatchingGray(Image *src, Image *template, Point *position,
                          double *distance, double threshold, Image *srcori,
                          Image *tempori) {
    if (src->channel != 1 || template->channel != 1) {
        fprintf(stderr, "src and/or templeta image is not a gray image.\n");
        return;
    }

    int min_distance = INT_MAX;
    int ret_x = 0;
    int ret_y = 0;
    int x, y, i, j;
    for (y = 0; y < (src->height - template->height); y++) {
        for (x = 0; x < src->width - template->width; x++) {
            int distance = 0;
            // SSD
            for (j = 0; j < template->height; j++) {
                for (i = 0; i < template->width; i++) {
                    // if (template->data[j * template->width + i] == 0)
                    //     continue;  // テンプレートが黒ならスキップ
                    int v = (src->data[(y + j) * src->width + (x + i)] -
                             template->data[j * template->width + i]);
                    distance += v * v;
                }
            }
            if (distance < min_distance) {
                min_distance = distance;
                ret_x = x;
                ret_y = y;
                if (distance < threshold) goto CUT;
            }
        }
    }
CUT:
    // 背景画像50%縮小の場合
    int src_size = src->height * src->width;
    if (src_size > 80000 && src_size < 180000) {
        int min_distance = INT_MAX;
        int pre_x = ret_x;
        int pre_y = ret_y;
        for (y = pre_y * 2 - 1; y < pre_y * 2 + 2; y++) {
            for (x = pre_x * 2 - 1; x < pre_x * 2 + 2; x++) {
                int distance = 0;
                // SSD
                for (j = 0; j < tempori->height; j++) {
                    for (i = 0; i < tempori->width; i++) {
                        // if (template->data[j * template->width + i] == 0)
                        //     continue;  // テンプレートが黒ならスキップ
                        int v =
                            (srcori->data[(y + j) * srcori->width + (x + i)] -
                             tempori->data[j * tempori->width + i]);
                        distance += v * v;
                    }
                }
                if (distance < min_distance) {
                    min_distance = distance;
                    ret_x = x;
                    ret_y = y;
                }
            }
        }
    }
    position->x = ret_x;
    position->y = ret_y;
    *distance = sqrt(min_distance) / (template->width * template->height);
}

void templateMatchingColor(Image *src, Image *template, Point *position,
                           double *distance) {
    if (src->channel != 3 || template->channel != 3) {
        fprintf(stderr, "src and/or templeta image is not a color image.\n");
        return;
    }

    int min_distance = INT_MAX;
    int ret_x = 0;
    int ret_y = 0;
    int x, y, i, j;
    for (y = 0; y < (src->height - template->height); y++) {
        for (x = 0; x < src->width - template->width; x++) {
            int distance = 0;
            // SSD
            for (j = 0; j < template->height; j++) {
                for (i = 0; i < template->width; i++) {
                    int pt = 3 * ((y + j) * src->width + (x + i));
                    int pt2 = 3 * (j * template->width + i);
                    int r = (src->data[pt + 0] - template->data[pt2 + 0]);
                    int g = (src->data[pt + 1] - template->data[pt2 + 1]);
                    int b = (src->data[pt + 2] - template->data[pt2 + 2]);

                    distance += (r * r + g * g + b * b);
                }
            }
            if (distance < min_distance) {
                min_distance = distance;
                ret_x = x;
                ret_y = y;
            }
        }
    }

    position->x = ret_x;
    position->y = ret_y;
    *distance = sqrt(min_distance) / (template->width * template->height);
}

// test/beach3.ppm template /airgun_women_syufu.ppm 0 0.5 cwp
int main(int argc, char **argv) {
    if (argc < 7) {
        fprintf(stderr,
                "Usage: templateMatching src_image temlate_image rotation "
                "threshold option(c,w,p,g)\n");
        fprintf(stderr,
                "Option:\nc) clear a txt result. \nw) write result a image "
                "with rectangle.\np) print results.\n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0  \n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 c\n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 w\n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 p\n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 g\n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 cw\n");
        fprintf(
            stderr,
            "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 cwp\n");
        fprintf(stderr,
                "ex: templateMatching src_image.ppm temlate_image.ppm 0 1.0 "
                "cwpg\n");
        return -1;
    }

    char *input_file = argv[1];
    char *template_file = argv[2];
    int rotation = atoi(argv[3]);
    double threshold = atof(argv[4]);
    char *input_file2 = argv[6];
    char *template_file2 = argv[7];

    printf("rotation -> %d\n", rotation);

    char output_name_base[256];
    char output_name_txt[256];
    char output_name_img[256];
    strcpy(output_name_base, "result/");
    strcat(output_name_base, getBaseName(input_file));
    strcpy(output_name_txt, output_name_base);
    strcat(output_name_txt, ".txt");
    strcpy(output_name_img, output_name_base);

    int isWriteImageResult = 0;
    int isPrintResult = 0;
    int isGray = 0;

    if (argc == 8) {
        char *p = NULL;
        if (p = strchr(argv[5], 'c') != NULL) clearResult(output_name_txt);
        if (p = strchr(argv[5], 'w') != NULL) isWriteImageResult = 1;
        if (p = strchr(argv[5], 'p') != NULL) isPrintResult = 1;
        if (p = strchr(argv[5], 'g') != NULL) isGray = 1;
    }

    Image *img = readPXM(input_file);
    Image *template = readPXM(template_file);
    Image *img2 = readPXM(input_file2);
    Image *template2 = readPXM(template_file2);

    Point result;
    double distance = 0.0;

    if (isGray && img->channel == 3) {
        Image *img_gray = createImage(img->width, img->height, 1);
        Image *img_gray2 = createImage(img2->width, img2->height, 1);
        Image *template_gray =
            createImage(template->width, template->height, 1);
        Image *template_gray2 =
            createImage(template2->width, template2->height, 1);
        cvtColorGray(img, img_gray);
        cvtColorGray(img2, img_gray2);
        cvtColorGray(template, template_gray);
        cvtColorGray(template2, template_gray2);

        templateMatchingGray(img_gray, template_gray, &result, &distance,
                             threshold, img_gray2, template_gray2);

        freeImage(img_gray);
        freeImage(img_gray2);
        freeImage(template_gray);
        freeImage(template_gray2);
    } else {
        templateMatchingColor(img, template, &result, &distance);
    }

    if (distance < threshold) {
        // int img_size = img->height * img->width;
        // if (img_size > 80000 && img_size < 180000) {
        //     template->width *= 2;
        //     template->height *= 2;
        // }
        writeResult(output_name_txt, getBaseName(template_file), result,
                    template2->width, template2->height, rotation, distance);
        if (isPrintResult) {
            printf("[Found    ] %s %d %d %d %d %d %f\n",
                   getBaseName(template_file), result.x, result.y,
                   template2->width, template2->height, rotation, distance);
        }
        if (isWriteImageResult) {
            drawRectangle(img, result, template2->width, template2->height);

            if (img->channel == 3)
                strcat(output_name_img, ".ppm");
            else if (img->channel == 1)
                strcat(output_name_img, ".pgm");
            printf("out: %s", output_name_img);
            writePXM(output_name_img, img);
        }
    } else {
        if (isPrintResult) {
            printf("%f %f[Not found] %s %d %d %d %d %d %f\n", distance,
                   threshold, getBaseName(template_file), result.x, result.y,
                   template2->width, template2->height, rotation, distance);
        }
    }

    freeImage(img);
    freeImage(img2);
    freeImage(template);
    freeImage(template2);

    return 0;
}
