package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.IOException;
import java.util.List;

public class CompositeAction extends AbstractAction {

    private final Action[] actions;
    private final boolean stopOnError;

    public CompositeAction(List list, boolean flag) {
        this.actions = new Action[list.size()];
        list.toArray(this.actions);
        this.stopOnError = flag;
    }

    public void run() {
        try {
            this.execute();
        } catch (IOException ioexception) {
            CompositeAction.LOGGER.warn("Exception during file rollover.", (Throwable) ioexception);
        }

    }

    public boolean execute() throws IOException {
        if (this.stopOnError) {
            Action[] aaction = this.actions;
            int i = aaction.length;

            for (int j = 0; j < i; ++j) {
                Action action = aaction[j];

                if (!action.execute()) {
                    return false;
                }
            }

            return true;
        } else {
            boolean flag = true;
            IOException ioexception = null;
            Action[] aaction1 = this.actions;
            int k = aaction1.length;

            for (int l = 0; l < k; ++l) {
                Action action1 = aaction1[l];

                try {
                    flag &= action1.execute();
                } catch (IOException ioexception1) {
                    flag = false;
                    if (ioexception == null) {
                        ioexception = ioexception1;
                    }
                }
            }

            if (ioexception != null) {
                throw ioexception;
            } else {
                return flag;
            }
        }
    }
}
