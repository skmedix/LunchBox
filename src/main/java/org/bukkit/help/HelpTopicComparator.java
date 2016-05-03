package org.bukkit.help;

import java.util.Comparator;

public class HelpTopicComparator implements Comparator {

    private static final HelpTopicComparator.TopicNameComparator tnc = new HelpTopicComparator.TopicNameComparator((HelpTopicComparator.TopicNameComparator) null);
    private static final HelpTopicComparator htc = new HelpTopicComparator();

    public static HelpTopicComparator.TopicNameComparator topicNameComparatorInstance() {
        return HelpTopicComparator.tnc;
    }

    public static HelpTopicComparator helpTopicComparatorInstance() {
        return HelpTopicComparator.htc;
    }

    public int compare(HelpTopic lhs, HelpTopic rhs) {
        return HelpTopicComparator.tnc.compare(lhs.getName(), rhs.getName());
    }

    public static class TopicNameComparator implements Comparator {

        private TopicNameComparator() {}

        public int compare(String lhs, String rhs) {
            boolean lhsStartSlash = lhs.startsWith("/");
            boolean rhsStartSlash = rhs.startsWith("/");

            return lhsStartSlash && !rhsStartSlash ? 1 : (!lhsStartSlash && rhsStartSlash ? -1 : lhs.compareToIgnoreCase(rhs));
        }

        TopicNameComparator(HelpTopicComparator.TopicNameComparator helptopiccomparator_topicnamecomparator) {
            this();
        }
    }
}
