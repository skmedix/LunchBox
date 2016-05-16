package org.bukkit.craftbukkit.v1_8_R3.help;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.command.VanillaCommandWrapper;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.HelpTopicFactory;
import org.bukkit.help.IndexHelpTopic;

import javax.annotation.Nullable;

public class SimpleHelpMap implements HelpMap {

    private HelpTopic defaultTopic;
    private final Map helpTopics = new TreeMap(HelpTopicComparator.topicNameComparatorInstance());
    private final Map topicFactoryMap = new HashMap();
    private final CraftServer server;
    private HelpYamlReader yaml;

    public SimpleHelpMap(CraftServer server) {
        this.server = server;
        this.yaml = new HelpYamlReader(server);
        Predicate indexFilter = Predicates.not(Predicates.instanceOf(CommandAliasHelpTopic.class));

        if (!this.yaml.commandTopicsInMasterIndex()) {
            indexFilter = Predicates.and(indexFilter, Predicates.not(new SimpleHelpMap.IsCommandTopicPredicate((SimpleHelpMap.IsCommandTopicPredicate) null)));
        }

        this.defaultTopic = new IndexHelpTopic("Index", (String) null, (String) null, Collections2.filter(this.helpTopics.values(), indexFilter), "Use /help [n] to get page n of help.");
        this.registerHelpTopicFactory(MultipleCommandAlias.class, new MultipleCommandAliasHelpTopicFactory());
    }

    public synchronized HelpTopic getHelpTopic(String topicName) {
        return topicName.equals("") ? this.defaultTopic : (this.helpTopics.containsKey(topicName) ? (HelpTopic) this.helpTopics.get(topicName) : null);
    }

    public Collection getHelpTopics() {
        return this.helpTopics.values();
    }

    public synchronized void addTopic(HelpTopic topic) {
        if (!this.helpTopics.containsKey(topic.getName())) {
            this.helpTopics.put(topic.getName(), topic);
        }

    }

    public synchronized void clear() {
        this.helpTopics.clear();
    }

    public List getIgnoredPlugins() {
        return this.yaml.getIgnoredPlugins();
    }

    public synchronized void initializeGeneralTopics() {
        this.yaml = new HelpYamlReader(this.server);
        Iterator iterator = this.yaml.getGeneralTopics().iterator();

        HelpTopic topic;

        while (iterator.hasNext()) {
            topic = (HelpTopic) iterator.next();
            this.addTopic(topic);
        }

        iterator = this.yaml.getIndexTopics().iterator();

        while (iterator.hasNext()) {
            topic = (HelpTopic) iterator.next();
            if (topic.getName().equals("Default")) {
                this.defaultTopic = topic;
            } else {
                this.addTopic(topic);
            }
        }

    }

    public synchronized void initializeCommands() {
        HashSet ignoredPlugins = new HashSet(this.yaml.getIgnoredPlugins());

        if (!ignoredPlugins.contains("All")) {
            Iterator pluginIndexes = this.server.getCommandMap().getCommands().iterator();

            Command filteredTopics;
            Iterator iterator;

            label82:
            while (pluginIndexes.hasNext()) {
                filteredTopics = (Command) pluginIndexes.next();
                if (!this.commandInIgnoredPlugin(filteredTopics, ignoredPlugins)) {
                    iterator = this.topicFactoryMap.keySet().iterator();

                    Class amendment;
                    HelpTopic t;

                    do {
                        if (!iterator.hasNext()) {
                            this.addTopic(new GenericCommandHelpTopic(filteredTopics));
                            continue label82;
                        }

                        amendment = (Class) iterator.next();
                        if (amendment.isAssignableFrom(filteredTopics.getClass())) {
                            t = ((HelpTopicFactory) this.topicFactoryMap.get(amendment)).createTopic(filteredTopics);
                            if (t != null) {
                                this.addTopic(t);
                            }
                            continue label82;
                        }
                    } while (!(filteredTopics instanceof PluginCommand) || !amendment.isAssignableFrom(((PluginCommand) filteredTopics).getExecutor().getClass()));

                    t = ((HelpTopicFactory) this.topicFactoryMap.get(amendment)).createTopic(filteredTopics);
                    if (t != null) {
                        this.addTopic(t);
                    }
                }
            }

            pluginIndexes = this.server.getCommandMap().getCommands().iterator();

            while (pluginIndexes.hasNext()) {
                filteredTopics = (Command) pluginIndexes.next();
                if (!this.commandInIgnoredPlugin(filteredTopics, ignoredPlugins)) {
                    iterator = filteredTopics.getAliases().iterator();

                    while (iterator.hasNext()) {
                        String amendment1 = (String) iterator.next();

                        if (this.server.getCommandMap().getCommand(amendment1) == filteredTopics) {
                            this.addTopic(new CommandAliasHelpTopic("/" + amendment1, "/" + filteredTopics.getLabel(), this));
                        }
                    }
                }
            }

            Collection filteredTopics1 = Collections2.filter(this.helpTopics.values(), Predicates.instanceOf(CommandAliasHelpTopic.class));

            if (!filteredTopics1.isEmpty()) {
                this.addTopic(new IndexHelpTopic("Aliases", "Lists command aliases", (String) null, filteredTopics1));
            }

            HashMap pluginIndexes1 = new HashMap();

            this.fillPluginIndexes(pluginIndexes1, this.server.getCommandMap().getCommands());
            iterator = pluginIndexes1.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry amendment2 = (Entry) iterator.next();

                this.addTopic(new IndexHelpTopic((String) amendment2.getKey(), "All commands for " + (String) amendment2.getKey(), (String) null, (Collection) amendment2.getValue(), "Below is a list of all " + (String) amendment2.getKey() + " commands:"));
            }

            iterator = this.yaml.getTopicAmendments().iterator();

            while (iterator.hasNext()) {
                HelpTopicAmendment amendment3 = (HelpTopicAmendment) iterator.next();

                if (this.helpTopics.containsKey(amendment3.getTopicName())) {
                    ((HelpTopic) this.helpTopics.get(amendment3.getTopicName())).amendTopic(amendment3.getShortText(), amendment3.getFullText());
                    if (amendment3.getPermission() != null) {
                        ((HelpTopic) this.helpTopics.get(amendment3.getTopicName())).amendCanSee(amendment3.getPermission());
                    }
                }
            }

        }
    }

    private void fillPluginIndexes(Map pluginIndexes, Collection commands) {
        Iterator iterator = commands.iterator();

        while (iterator.hasNext()) {
            Command command = (Command) iterator.next();
            String pluginName = this.getCommandPluginName(command);

            if (pluginName != null) {
                HelpTopic topic = this.getHelpTopic("/" + command.getLabel());

                if (topic != null) {
                    if (!pluginIndexes.containsKey(pluginName)) {
                        pluginIndexes.put(pluginName, new TreeSet(HelpTopicComparator.helpTopicComparatorInstance()));
                    }

                    ((Set) pluginIndexes.get(pluginName)).add(topic);
                }
            }
        }

    }

    private String getCommandPluginName(Command command) {
        return command instanceof VanillaCommandWrapper ? "Minecraft" : (!(command instanceof BukkitCommand) && !(command instanceof VanillaCommand) ? (command instanceof PluginIdentifiableCommand ? ((PluginIdentifiableCommand) command).getPlugin().getName() : null) : "Bukkit");
    }

    private boolean commandInIgnoredPlugin(Command command, Set ignoredPlugins) {
        return (command instanceof BukkitCommand || command instanceof VanillaCommand) && ignoredPlugins.contains("Bukkit") ? true : command instanceof PluginIdentifiableCommand && ignoredPlugins.contains(((PluginIdentifiableCommand) command).getPlugin().getName());
    }

    public void registerHelpTopicFactory(Class commandClass, HelpTopicFactory factory) {
        if (!Command.class.isAssignableFrom(commandClass) && !CommandExecutor.class.isAssignableFrom(commandClass)) {
            throw new IllegalArgumentException("commandClass must implement either Command or CommandExecutor!");
        } else {
            this.topicFactoryMap.put(commandClass, factory);
        }
    }

    private class IsCommandTopicPredicate implements Predicate {

        private IsCommandTopicPredicate() {}

        public boolean apply(HelpTopic topic) {
            return topic.getName().charAt(0) == 47;
        }

        IsCommandTopicPredicate(SimpleHelpMap.IsCommandTopicPredicate simplehelpmap_iscommandtopicpredicate) {
            this();
        }

        public boolean apply(@Nullable Object input) { return false; }
    }
}
