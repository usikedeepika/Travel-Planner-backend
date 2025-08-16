
package org.example.service;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextProcessingService {
    private final Analyzer analyzer;
    public TextProcessingService(){
        analyzer=new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                StandardTokenizer tokenizer=new StandardTokenizer();
                TokenStream filter = new LowerCaseFilter(tokenizer);
                filter = new PorterStemFilter(filter);
                return new TokenStreamComponents(tokenizer, filter);
            }
        };
    }
    public List<String> analyzeText(String text) throws IOException {
        List<String > tokens=new ArrayList<>();
        try(TokenStream tokenStream = analyzer.tokenStream("", new StringReader(text))){
            CharTermAttribute termAttribute=tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()){
                String token = termAttribute.toString();
                if(!token.trim().isEmpty()){
                    tokens.add(token);
                }
            }
            tokenStream.end();
        }
        return tokens;
    }
    public String processText(String text){
        if(text==null || text.trim().isEmpty())
            return "";
        try{
            List<String> processedTokens = analyzeText(text);
            return String.join(" ", processedTokens);
        }catch (Exception e){
            return text.toLowerCase();
        }
    }
}