package com.example.demo.controller;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.io.InputStream;


@Controller
public class WordCountController {

    private static final String MODEL_PATH = "en-sent.bin";
    private final SentenceDetector sentenceDetector;

    public WordCountController() throws Exception {
        InputStream modelIn = new FileInputStream(MODEL_PATH);
        SentenceModel model = new SentenceModel(modelIn);
        sentenceDetector = new SentenceDetectorME(model);
    }

    @PostMapping("/count")
    public String wordCount(@RequestParam("text") String text, Model model) {
        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        model.addAttribute("wordCount", wordCount);
        model.addAttribute("sentenceCount", sentenceCount);
        return "nlpPages/wordCount";
    }

    @GetMapping("count")
    public String showResult(){
        return "nlpPages/wordCount";
    }

    public static int countWords(String text) {
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);
        int count = 0;
        for (String token : tokens) {
            // Исключаем пунктуацию из подсчета
            if (!Character.isLetterOrDigit(token.charAt(0))) {
                continue;
            }
            count++;
        }
        return count;
        //byhbjbhj
    }
    private int countSentences(String text) {
        String[] sentences = sentenceDetector.sentDetect(text);
        return sentences.length;
    }

}
