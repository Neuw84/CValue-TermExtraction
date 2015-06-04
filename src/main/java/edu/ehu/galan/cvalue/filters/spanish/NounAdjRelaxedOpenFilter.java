package edu.ehu.galan.cvalue.filters.spanish;
/*
 * Copyright (C) 2014 Angel Conde Manjon neuw84 at gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.Token;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements an open filter based in this regular expresion
 * ((<Noun><Preposition>|<Noun>)(<Adjetive>)?(<Noun>)?(<<Adjetive>>)?)+
 * using POS tags of Freeling (EAGLES spanish tagset)
 * @author Angel Conde Manjon
 */

public class NounAdjRelaxedOpenFilter implements ILinguisticFilter {

    private List<String> list;

    @Override
    public List<String> getCandidates(LinkedList<Token> pSentence) {
        list = new ArrayList<>();
        if (pSentence != null) {
            for (Token token : pSentence) {
                if (token.getPosTag().matches("N.*")) {
                    int pos = pSentence.indexOf(token);
                    findCandidate(pSentence, pos);
                    
                }
            }
        }
        return list;
    }

    private void findCandidate(LinkedList<Token> pSentence, int pPos) {
        Pattern pat = Pattern.compile("((<N......><SP...>|<N......>)(<A.....>)?(<N......>)?(<A.....>)?)+");
        int sentenceSize = pSentence.size();
        StringBuilder sb = new StringBuilder();
        int j = 0;
        sb.append("<").append(pSentence.get(pPos).getPosTag()).append(">");
        for (int i = pPos + 1; i < pSentence.size(); i++) {
            sb.append("<").append(pSentence.get(i).getPosTag()).append(">");
            Matcher mat = pat.matcher(sb.toString());
            if (mat.matches()) {
                String term = "";
                for (int x = pPos; x <= i; x++) {
                    term = term + pSentence.get(x).getWordForm() + " ";
                }
                list.add(term.trim());

            }
        }

    }

 
}
