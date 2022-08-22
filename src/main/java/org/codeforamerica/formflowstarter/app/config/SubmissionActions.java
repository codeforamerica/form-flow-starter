package org.codeforamerica.formflowstarter.app.config;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.commons.digester.RegexMatcher;
import org.codeforamerica.formflowstarter.app.data.Submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubmissionActions {
    public static void clearIncomeAmountsBeforeSaving(Submission submission, String uuid) {
        //grab the current household members incometypes
        var entryByUuid = Submission.getSubflowEntryByUuid("income", uuid, submission);
        var incomeAmounts = entryByUuid.entrySet().stream()
                .filter(e -> e.getKey().matches(".*Amount$")).map(Map.Entry::getKey).toList();

        var incomeTypesArray = (ArrayList<String>) entryByUuid.get("incomeTypes[]");

        incomeAmounts.stream().forEach(incomeAmount -> {
            if (!incomeTypesArray.contains(incomeAmount.replace("Amount", ""))) {
                entryByUuid.remove(incomeAmount);
            }
        });
    }
}
