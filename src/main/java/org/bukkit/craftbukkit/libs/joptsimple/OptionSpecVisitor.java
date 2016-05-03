package org.bukkit.craftbukkit.libs.joptsimple;

interface OptionSpecVisitor {

    void visit(NoArgumentOptionSpec noargumentoptionspec);

    void visit(RequiredArgumentOptionSpec requiredargumentoptionspec);

    void visit(OptionalArgumentOptionSpec optionalargumentoptionspec);

    void visit(AlternativeLongOptionSpec alternativelongoptionspec);
}
