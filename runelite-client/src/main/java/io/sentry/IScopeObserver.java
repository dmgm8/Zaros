/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.Breadcrumb;
import io.sentry.protocol.User;

public interface IScopeObserver {
    public void setUser(User var1);

    public void addBreadcrumb(Breadcrumb var1);

    public void setTag(String var1, String var2);

    public void removeTag(String var1);

    public void setExtra(String var1, String var2);

    public void removeExtra(String var1);
}

