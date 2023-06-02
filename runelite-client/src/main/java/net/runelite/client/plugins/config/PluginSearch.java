/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  org.apache.commons.lang3.StringUtils
 */
package net.runelite.client.plugins.config;

import com.google.common.base.Splitter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.runelite.client.plugins.config.SearchablePlugin;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;

public class PluginSearch {
    private static final Splitter SPLITTER = Splitter.on((String)" ").trimResults().omitEmptyStrings();

    public static <T extends SearchablePlugin> List<T> search(Collection<T> searchablePlugins, String query) {
        return searchablePlugins.stream().filter(plugin -> Text.matchesSearchTerms(SPLITTER.split((CharSequence)query.toLowerCase()), plugin.getKeywords())).sorted(PluginSearch.comparator(query)).collect(Collectors.toList());
    }

    private static Comparator<SearchablePlugin> comparator(String query) {
        if (StringUtils.isBlank((CharSequence)query)) {
            return Comparator.nullsLast(Comparator.comparing(SearchablePlugin::isPinned, Comparator.nullsLast(Comparator.reverseOrder()))).thenComparing(SearchablePlugin::getSearchableName, Comparator.nullsLast(Comparator.naturalOrder()));
        }
        Iterable queryPieces = SPLITTER.split((CharSequence)query.toLowerCase());
        return Comparator.nullsLast(Comparator.comparing(sp -> query.equalsIgnoreCase(sp.getSearchableName()), Comparator.reverseOrder())).thenComparing(sp -> {
            if (sp.getSearchableName() == null) {
                return 0L;
            }
            return PluginSearch.stream(SPLITTER.split((CharSequence)sp.getSearchableName())).filter(piece -> PluginSearch.stream(queryPieces).anyMatch(qp -> PluginSearch.containsOrIsContainedBy(piece.toLowerCase(), qp))).count();
        }, Comparator.reverseOrder()).thenComparing(sp -> {
            if (sp.getKeywords() == null) {
                return 0L;
            }
            return PluginSearch.stream(sp.getKeywords()).filter(piece -> PluginSearch.stream(queryPieces).anyMatch(qp -> PluginSearch.containsOrIsContainedBy(piece.toLowerCase(), qp))).count();
        }, Comparator.reverseOrder()).thenComparing(SearchablePlugin::isPinned, Comparator.nullsLast(Comparator.reverseOrder())).thenComparing(SearchablePlugin::getSearchableName, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    private static Stream<String> stream(Iterable<String> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private static boolean containsOrIsContainedBy(String a, String b) {
        return a.contains(b) || b.contains(a);
    }
}

