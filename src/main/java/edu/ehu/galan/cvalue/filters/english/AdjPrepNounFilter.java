package edu.ehu.galan.cvalue.filters.english;

/*
 *    AdjPrepNounFilter.java
 *    Copyright (C) 2013 Angel Conde, neuw84 at gmail dot com
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.Token;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Filter that searchs for ((ADJ|NOUN)+|(ADJ|NOUN)*(NOUNPREP)?(ADJ|NOUN)*)NOUN
 *
 * @author angel conde manjon
 */
public class AdjPrepNounFilter implements ILinguisticFilter {

    private List<String> list;
    private List<Token> listTokens;

    @Override
    public List<String> getCandidates(LinkedList<Token> pSentence) {
        list = new ArrayList<>();
        if (pSentence != null) {
            for (Token token : pSentence) {
                if (token.getPosTag().matches("JJ|JJS|NN|NNS|NNP|NNPS")) {
                    String candidate = null;
                    listTokens = new ArrayList<>();
                    listTokens.add(token);
                    int pos = pSentence.indexOf(token);
                    String result = findCandidate(pSentence, pos, candidate);
                    if (result != null) {
                        if (finishWithNoun() && onlyPrep()) {
                            list.add(result);
                        }
                    }
                }
            }
        }
        return list;

    }

    private String findCandidate(LinkedList<Token> pSentence, int pPos, String candidate) {
        int sentenceSize = pSentence.size();
        Token word = null;
        if (sentenceSize - 1 > pPos) {
            word = pSentence.get(pPos + 1);
        }
        if (word != null) {

            if (word.getPosTag().matches("JJ|JJS|NN|NNS|NNP|NNPS")) {
                candidate = pSentence.get(pPos).getWordForm() + " " + word.getWordForm();
                listTokens.add(word);
                if (finishWithNoun() && onlyPrep()) {
                    list.add(candidate);
                }
                pPos++;
                if (sentenceSize - 1 == pPos) {
                    candidate = null;
                } else {
                    if (word.getPosTag().matches("NN|NNS|NNP|NNPS") && pSentence.get(pPos).getPosTag().matches("IN")) {
                        Token prep = pSentence.get(pPos);
                        candidate = candidate + " " + prep.getWordForm();
                        if (finishWithNoun() && onlyPrep()) {
                            list.add(candidate);
                        }
                        word = prep;
                        return findCandidate2(pSentence, pPos, candidate);
                    } else if (!(pSentence.get(pPos).getPosTag().matches("JJ|JJS|NN|NNS|NNP|NNPS"))) {
                        return null;
                    }
                }
                return findCandidate2(pSentence, pPos, candidate);
            } else {
                return candidate;
            }
        } else {
            return candidate;
        }


    }

    private String findCandidate2(LinkedList<Token> pSentence, int pPos, String candidate) {
        int sentenceSize = pSentence.size();
        Token word = null;
        if (sentenceSize - 1 > pPos) {
            word = pSentence.get(pPos + 1);

        }
        if (word != null) {
            if (word.getPosTag().matches("JJ|JJS|NN|NNS|NNP|NNPS")) {

                candidate = candidate + " " + word.getWordForm();
                listTokens.add(word);
                if (finishWithNoun() && onlyPrep()) {
                    list.add(candidate);
                }
                pPos++;
                if (sentenceSize - 1 == pPos) {
                    candidate = null;
                } else {
                    if (word.getPosTag().matches("NN|NNS|NNP|NNPS") && pSentence.get(pPos).getPosTag().matches("IN")) {
                        Token prep = pSentence.get(pPos);
                        candidate = candidate + " " + prep.getWordForm();
                        if (finishWithNoun() && onlyPrep()) {
                            list.add(candidate);
                        }
                        return findCandidate2(pSentence, pPos, candidate);
                    } else if (!(pSentence.get(pPos).getPosTag().matches("JJ|JJS|NN|NNS|NNP|NNPS"))) {
                        return null;
                    }
                }
                        return findCandidate2(pSentence, pPos, candidate);
            } else {
                return candidate;
            }
        } else {
            return candidate;
        }
    }

    private boolean finishWithNoun() {
        if (listTokens.get(listTokens.size() - 1).getPosTag().matches("NNS|NN|NNP|NNPS")) {
            return true;
        }
        return false;
    }

    private boolean onlyPrep() {
        int count = 0;
        for (Token string : listTokens) {
            if (string.getPosTag().equalsIgnoreCase("IN")) {
                count++;
            }
        }
        if (count == 0 | count == 1) {
            return true;
        } else {
            return false;
        }
    }
    
//     public static void main(String[] args) {
//         LinkedList<Token> tokenList=new LinkedList<>();
//         tokenList.add(new Token("hello", "NN"));
//         tokenList.add(new Token("hello", "NN"));
//         new AdjPrepNounFilter().getCandidates(tokenList);
//         
//    }

}