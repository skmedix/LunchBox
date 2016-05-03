package org.bukkit.help;

import java.util.Collection;
import java.util.List;

public interface HelpMap {

    HelpTopic getHelpTopic(String s);

    Collection getHelpTopics();

    void addTopic(HelpTopic helptopic);

    void clear();

    void registerHelpTopicFactory(Class oclass, HelpTopicFactory helptopicfactory);

    List getIgnoredPlugins();
}
