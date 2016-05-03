package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.NoSuchElementException;

class OptionSpecTokenizer {

    private static final char POSIXLY_CORRECT_MARKER = '+';
    private String specification;
    private int index;

    OptionSpecTokenizer(String specification) {
        if (specification == null) {
            throw new NullPointerException("null option specification");
        } else {
            this.specification = specification;
        }
    }

    boolean hasMore() {
        return this.index < this.specification.length();
    }

    AbstractOptionSpec next() {
        if (!this.hasMore()) {
            throw new NoSuchElementException();
        } else {
            String optionCandidate = String.valueOf(this.specification.charAt(this.index));

            ++this.index;
            if ("W".equals(optionCandidate)) {
                AbstractOptionSpec spec = this.handleReservedForExtensionsToken();

                if (spec != null) {
                    return spec;
                }
            }

            ParserRules.ensureLegalOption(optionCandidate);
            Object object;

            if (this.hasMore()) {
                object = this.specification.charAt(this.index) == 58 ? this.handleArgumentAcceptingOption(optionCandidate) : new NoArgumentOptionSpec(optionCandidate);
            } else {
                object = new NoArgumentOptionSpec(optionCandidate);
            }

            return (AbstractOptionSpec) object;
        }
    }

    void configure(OptionParser parser) {
        this.adjustForPosixlyCorrect(parser);

        while (this.hasMore()) {
            parser.recognize(this.next());
        }

    }

    private void adjustForPosixlyCorrect(OptionParser parser) {
        if (43 == this.specification.charAt(0)) {
            parser.posixlyCorrect(true);
            this.specification = this.specification.substring(1);
        }

    }

    private AbstractOptionSpec handleReservedForExtensionsToken() {
        if (!this.hasMore()) {
            return new NoArgumentOptionSpec("W");
        } else if (this.specification.charAt(this.index) == 59) {
            ++this.index;
            return new AlternativeLongOptionSpec();
        } else {
            return null;
        }
    }

    private AbstractOptionSpec handleArgumentAcceptingOption(String candidate) {
        ++this.index;
        if (this.hasMore() && this.specification.charAt(this.index) == 58) {
            ++this.index;
            return new OptionalArgumentOptionSpec(candidate);
        } else {
            return new RequiredArgumentOptionSpec(candidate);
        }
    }
}
