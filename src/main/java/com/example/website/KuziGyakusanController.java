package com.example.website;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class KuziGyakusanController {
    private static final int SCALE = 20; // BigDecimalの計算精度を指定
    private static final RoundingMode ROUND = RoundingMode.HALF_UP; // BigDecimalの丸めモードを指定

    @GetMapping("/KuziGyakusan")
    public String showKujiForm() {
        return "KuziGyakusan"; // /kuziへのgetリクエストがあった際に、（kuzi.html）を表示
    }
    @PostMapping("/KuziGyakusan") ///POSTリクエストがあった場合にフォームからのデータを受け取り、確率計算を行う。
    public String calcResult(@RequestParam int total,
                         @RequestParam int atari,
                         Model model) {
        //エラーがあっても入力値を保持するためにモデルに追加
        model.addAttribute("total", total);
        model.addAttribute("atari", atari);
        // 入力検証
        if (total <= 0) {
            model.addAttribute("error", "「残りくじ総数」は1以上でなければなりません。");
            return "KuziGyakusan";
        }
        if (atari > total) {
            model.addAttribute("error", "「当たり枚数」は「残りくじ総数」以下である必要があります。");
            return "KuziGyakusan";
        }
        Map<Integer, BigDecimal> probs = new HashMap<>();
        int k = 0; // 購入数（試行回数）
        BigDecimal prob = BigDecimal.ZERO; // 当たりを引く確率
         try {
            while (k < total) {
            k++;
            BigInteger combTotal = KuziController.combination(total, k); //k回引く時の組み合わせ総数
            BigDecimal combTotalBD = new BigDecimal(combTotal);
            BigInteger combFailBI =  KuziController.combination(total - atari, k); //k回引いて全てハズレの組み合わせ数
            BigDecimal combFailBD = new BigDecimal(combFailBI);
            prob = BigDecimal.ONE.subtract(combFailBD.divide(combTotalBD, SCALE, ROUND)); //あたりを引く確率
            probs.put(k, prob.stripTrailingZeros());
            System.out.println("--- probs Map の中身 ---");
            probs.forEach((key, value) -> {
            // 購入数 (key) と確率 (value) を出力
            System.out.println("k=" + key + ", prob=" + value.toPlainString());
            });
        }
            model.addAttribute("probs", probs);
            return "KuziGyakusan";
        } catch (Exception ex) {
            model.addAttribute("error", "予期せぬエラーが発生しました。");
            return "KuziGyakusan";
        }
      
    }
    

}

