package com.example.website;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class KuziGyakusanController {
    private static final int SCALE = 20; // BigDecimalの計算精度を指定
    private static final RoundingMode ROUND = RoundingMode.DOWN; // BigDecimalの丸めモードを指定

    @GetMapping("/KuziGyakusan")
    public String showKujiForm(Model model) {
        model.addAttribute("Digits", 4); // 初期表示時に小数点以下4桁を設定
        return "KuziGyakusan"; // /kuziへのgetリクエストがあった際に、（kuzi.html）を表示
    }
    @PostMapping("/KuziGyakusan") ///POSTリクエストがあった場合にフォームからのデータを受け取り、確率計算を行う。
    public String calcResult(@RequestParam int total,
                         @RequestParam int atari,
                         @RequestParam int Digits,
                         Model model) {
        //エラーがあっても入力値を保持するためにモデルに追加
        model.addAttribute("total", total);
        model.addAttribute("atari", atari);
        model.addAttribute("Digits", Digits);
        // 入力検証
        if (total <= 0) {
            model.addAttribute("error", "「残りくじ総数」は1以上でなければなりません。");
            return "KuziGyakusan";
        }
        if (atari > total) {
            model.addAttribute("error", "「当たり枚数」は「残りくじ総数」以下である必要があります。");
            return "KuziGyakusan";
        }
        Map<Integer, BigDecimal> Probs = new HashMap<>();
        Map<Integer, BigDecimal> KeyProbs = new LinkedHashMap<>();
        int k = 0; // 購入数（試行回数）
        BigDecimal Prob = BigDecimal.ZERO; // 当たりを引く確率
        int threshold = 10;
         try {
            while (k < total) {
            k++;
            BigInteger combTotal = KuziController.combination(total, k); //k回引く時の組み合わせ総数
            BigDecimal combTotalBD = new BigDecimal(combTotal);
            BigInteger combFailBI =  KuziController.combination(total - atari, k); //k回引いて全てハズレの組み合わせ数
            BigDecimal combFailBD = new BigDecimal(combFailBI);
            Prob = BigDecimal.ONE.subtract(combFailBD.divide(combTotalBD, SCALE, ROUND)).multiply(new BigDecimal(100)); //あたりを引く確率
            BigDecimal ProbDown = Prob.setScale(Digits, RoundingMode.DOWN);
            Probs.put(k, ProbDown.stripTrailingZeros());

                if (ProbDown.compareTo(new BigDecimal(threshold)) >= 0) {// キリ番ごとの、kとProbDownをkeyProbsに保存
                KeyProbs.put(k, ProbDown.stripTrailingZeros());
                threshold += 10;
                }
            }
            model.addAttribute("KeyProbs", KeyProbs);
            model.addAttribute("Probs", Probs);
            return "KuziGyakusan";
        } catch (Exception ex) {
            model.addAttribute("error", "予期せぬエラーが発生しました。");
            return "KuziGyakusan";
        }
      
    }
    

}

