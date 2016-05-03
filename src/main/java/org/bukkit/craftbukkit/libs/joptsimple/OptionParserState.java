package org.bukkit.craftbukkit.libs.joptsimple;

abstract class OptionParserState {

    static OptionParserState noMoreOptions() {
        return new OptionParserState() {
            protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
                detectedOptions.addNonOptionArgument(arguments.next());
            }
        };
    }

    static OptionParserState moreOptions(final boolean posixlyCorrect) {
        return new OptionParserState() {
            protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
                String candidate = arguments.next();

                if (ParserRules.isOptionTerminator(candidate)) {
                    parser.noMoreOptions();
                } else if (ParserRules.isLongOptionToken(candidate)) {
                    parser.handleLongOptionToken(candidate, arguments, detectedOptions);
                } else if (ParserRules.isShortOptionToken(candidate)) {
                    parser.handleShortOptionToken(candidate, arguments, detectedOptions);
                } else {
                    if (posixlyCorrect) {
                        parser.noMoreOptions();
                    }

                    detectedOptions.addNonOptionArgument(candidate);
                }

            }
        };
    }

    protected abstract void handleArgument(OptionParser optionparser, ArgumentList argumentlist, OptionSet optionset);
}
