package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.Collection;

public class OptionSpecBuilder extends NoArgumentOptionSpec {

    private final OptionParser parser;

    OptionSpecBuilder(OptionParser parser, Collection options, String description) {
        super(options, description);
        this.parser = parser;
        this.attachToParser();
    }

    private void attachToParser() {
        this.parser.recognize(this);
    }

    public ArgumentAcceptingOptionSpec withRequiredArg() {
        RequiredArgumentOptionSpec newSpec = new RequiredArgumentOptionSpec(this.options(), this.description());

        this.parser.recognize(newSpec);
        return newSpec;
    }

    public ArgumentAcceptingOptionSpec withOptionalArg() {
        OptionalArgumentOptionSpec newSpec = new OptionalArgumentOptionSpec(this.options(), this.description());

        this.parser.recognize(newSpec);
        return newSpec;
    }
}
