package com.example.website;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class MonochromeController {
    @GetMapping("/Monochrome")
    public String showMonochromePage() {
        return "Monochrome";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("imageFile") MultipartFile file, Model model) {

        // 1. アップロードされたファイルが空でないかチェック
        if (file.isEmpty()) {
            model.addAttribute("message", "ファイルが選択されていません。");
            return "error"; // エラーページ（後で作成）に移動
        }

        try {
            // 2. アップロードされたファイルをサーバー上の一時的な場所に保存する
            // （ここでは "uploads" というフォルダに保存する例）
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir); // uploadsフォルダがなければ作成
            }
            Path inputFile = uploadDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), inputFile, StandardCopyOption.REPLACE_EXISTING);


            // 3. C++のプログラムを呼び出す準備
            String outputFileName = "processed_" + file.getOriginalFilename();
            Path outputFile = uploadDir.resolve(outputFileName);

           
            String cppExecutablePath = "/Users/yuya/個人プログラム/website/Website/cpp-processor/image_processor"; 

            ProcessBuilder processBuilder = new ProcessBuilder(
                cppExecutablePath,
                inputFile.toAbsolutePath().toString(),
                outputFile.toAbsolutePath().toString(),
                "MONOCHROME" 
            );

            // 4. C++プログラムを実行し、終わるまで待つ
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // C++の処理が終わるのを待機

            // 5. C++の処理結果に応じて、表示するページを分ける
            if (exitCode == 0) {
                // 成功した場合: 結果表示ページに画像パスを渡す
                model.addAttribute("processedImagePath", "/" + outputFile.toString());
                return "result"; // result.html を表示
            } else {
                // 失敗した場合: エラーメッセージを表示
                model.addAttribute("message", "画像の処理に失敗しました。");
                return "error"; // error.html を表示
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            model.addAttribute("message", "サーバーエラーが発生しました。");
            return "error";
        }
    }
    
}