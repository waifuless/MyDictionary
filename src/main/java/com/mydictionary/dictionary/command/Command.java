package com.mydictionary.dictionary.command;

public interface Command {

    CommandResponse execute(CommandRequest request);
}
