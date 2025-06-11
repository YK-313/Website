#include <iostream>
#include <string>
#include <vector>
#include <fstream>

int main(int argc, char* argv[]) {

    if (argc != 4) {
        std::cerr << "Error: Invalid number of arguments. Expected 3, got " << argc - 1 << std::endl;
        return 1; 
    }

    std::string inputFile = argv[1];
    std::string outputFile = argv[2];
    std::string command = argv[3];

    std::cout << "C++ Program Started" << std::endl;
    std::cout << "Input File: " << inputFile << std::endl;
    std::cout << "Output File: " << outputFile << std::endl;

    // ↓↓↓ この行のタイプミスを修正しました！ ↓↓↓
    std::cout << "Command: " << command << std::endl;

    std::ifstream src(inputFile, std::ios::binary);
    std::ofstream dst(outputFile, std::ios::binary);
    dst << src.rdbuf();

    std::cout << "C++ Program Finished Successfully" << std::endl;
    return 0;
}