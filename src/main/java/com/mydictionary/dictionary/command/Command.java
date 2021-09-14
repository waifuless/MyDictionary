package com.mydictionary.dictionary.command;

import java.util.Map;

public interface Command {

    CommandResponse execute(Map<String, String[]> parameterMap);
}
