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
@Controller //KuziControllerという名前のクラスを定義(Spring MVCのコントローラとして登録)
public class KuziController {

    private static final int SCALE = 20; // BigDecimalの計算精度を指定
    private static final RoundingMode ROUND = RoundingMode.HALF_UP; // BigDecimalの丸めモードを指定

    @GetMapping("/kuzi")
    public String showKujiForm() {
        return "kuzi"; // /kuziへのgetリクエストがあった際に、（kuzi.html）を表示
    }
    @PostMapping("/kuzi") ///POSTリクエストがあった場合にフォームからのデータを受け取り、確率計算を行う。
    public String calcResult(@RequestParam int total,
                         @RequestParam int atari,
                         @RequestParam int buy,
                         Model model) {
        //エラーがあっても入力値を保持するためにモデルに追加
        model.addAttribute("total", total);
        model.addAttribute("atari", atari);
        model.addAttribute("buy", buy);
        // 入力検証
        if (total <= 0) {
            model.addAttribute("error", "「残りくじ総数」は1以上でなければなりません。");
            return "kuzi";
        }
        if (atari < 0 || buy < 0) {
            model.addAttribute("error", "「当たり枚数」と「購入数」は0以上の整数を指定してください。");
            return "kuzi";
        }
        if (atari > total) {
            model.addAttribute("error", "「当たり枚数」は「残りくじ総数」以下である必要があります。");
            return "kuzi";
        }
        if (buy > total) {
            model.addAttribute("error", "「購入数」は「残りくじ総数」以下である必要があります。");
            return "kuzi";
        }
         try {
            BigInteger combTotal = combination(total, buy);
            BigDecimal combTotalBD = new BigDecimal(combTotal);

            // pA = 1 - C(total - atari, buy) / C(total, buy) [ハズレを引く確率の余事象を計算]
            BigInteger combFailBI = combination(total - atari, buy);
            BigDecimal combFailBD = new BigDecimal(combFailBI);
            BigDecimal pAFraction = BigDecimal.ONE; // あたりを引く確率の初期値1とし、下の条件でハズレがある場合に計算
            if (combFailBD.compareTo(BigDecimal.ZERO) > 0) {
                pAFraction = BigDecimal.ONE.subtract(
                        combFailBD.divide(combTotalBD, SCALE, ROUND)
                );
            } 
            BigDecimal pAPercent = pAFraction.multiply(BigDecimal.valueOf(100));

            Map<Integer, BigDecimal> probs = new HashMap<>();
            int maxN = Math.min(buy, atari);

            for (int n = 0; n <= maxN; n++) {
                BigInteger comb1 = combination(atari, n);
                BigInteger comb2 = combination(total - atari, buy - n);
                BigInteger numerator = comb1.multiply(comb2);

                BigDecimal prob = new BigDecimal(numerator)
                        .divide(combTotalBD, SCALE, ROUND)
                        .multiply(BigDecimal.valueOf(100));

                probs.put(n, prob.stripTrailingZeros());
            }

            model.addAttribute("pA", pAPercent.stripTrailingZeros());
            model.addAttribute("probs", probs);
            return "kuzi";
        } catch (Exception ex) {
            model.addAttribute("error", "予期せぬエラーが発生しました。");
            return "kuzi";
        }
      
    }
     // nCrを計算するメソッド(計算結果が大きくなる可能性があるため、任意精度の整数を扱えるBigIntegerを使用)
    public static BigInteger combination(int n, int r) {
    if (r < 0 || r > n) return BigInteger.ZERO; //例外処理
    if (r == 0 || r == n) return BigInteger.ONE; //計算が不要な場合の戻り値

    r = Math.min(r, n - r); //組み合わせ計算の対象性を利用して計算量を削減
    BigInteger result = BigInteger.ONE; //値の初期値
    for (int i = 1; i <= r; i++) {
        result = result.multiply(BigInteger.valueOf(n - i + 1)).divide(BigInteger.valueOf(i));
    }
    return result;
    }

}


