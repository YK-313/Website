#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include <opencv2/opencv.hpp>
int main(int argc, char* argv[]) {

    if (argc != 4) {
        std::cerr << "Error: Invalid number of arguments. Expected 3, got " << argc - 1 << std::endl;
        return 1; 
    }

    std::string inputFile = argv[1];
    std::string outputFile = argv[2];
    std::string command = argv[3];

    
    try {
        // === ここからがOpenCVを使った画像処理です ===

        // 1. 入力画像を読み込む
        cv::Mat inputImage = cv::imread(inputFile);

        // 読み込みに失敗したかチェック
        if (inputImage.empty()) {
            std::cerr << "Error: Could not open or find the image." << std::endl;
            return 1;
        }

       
        cv::Mat grayImage;

        
        cv::cvtColor(inputImage, grayImage, cv::COLOR_BGR2GRAY);

        
        cv::imwrite(outputFile, grayImage);

    } catch (const cv::Exception& e) {
        // OpenCV関連でエラーが起きた場合
        std::cerr << "OpenCV Error: " << e.what() << std::endl;
        return 1;
    }

    std::cout << "Image processed successfully with OpenCV." << std::endl;
    // 正常終了
    return 0;
}