package mandala.tsubuyaki

import com.atilika.kuromoji.TokenizerBase.Mode
import com.atilika.kuromoji.ipadic.{ Token, Tokenizer }

object CustomTokenizer {

  def tokenize(text: String, dictPath: String): java.util.List[Token] = {
    val builder = new Tokenizer.Builder().mode(Mode.SEARCH)
    val tokenizer: Tokenizer = builder.userDictionary(dictPath).build()

    tokenizer.tokenize(text)
  }
}
