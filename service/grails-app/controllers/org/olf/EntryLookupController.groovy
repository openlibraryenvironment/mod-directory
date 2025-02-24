package org.olf

import grails.gorm.multitenancy.Tenants
import org.olf.okapi.modules.directory.DirectoryEntry
import org.olf.okapi.modules.directory.Symbol;

import grails.converters.JSON;

class EntryLookupController {
    static final String OKAPITENANTHEADER = "X-Okapi-Tenant"

    def index() {
        String tenant = request.getHeader(OKAPITENANTHEADER);
        String symbolsString = params.symbols;
        String type = params.type; //typically "institution" or "branch"
        List<DirectoryEntry> result = [];

        Tenants.withId(tenant.toLowerCase()+'_mod_directory', {
            List<Symbol> symbolList = symbolsFromString(symbolsString);
            for (Symbol sym : symbolList) {
                result.add(sym.owner);
            }

            if (type) {
                List<DirectoryEntry> filteredEntries = [];
                for (DirectoryEntry de : result) {
                    if (de.type?.value == type) {
                        filteredEntries.add(de);
                    }
                }
                result = filteredEntries;
            }


        });

        render result as JSON;
    }

    List<Symbol> symbolsFromString(String symString) {
        List<Symbol> results = [];

        if (symString) {
            List<String> symbolStringList = symString.split(",");
            for (String sub : symbolStringList) {
                List<String> parts = sub.split(":", 2);
                if (parts?.size() == 2) {
                    Symbol resolvedSymbol = Symbol.findBySymbolAndAuthority(parts[1]?.toUpperCase(), parts[0]?.toUpperCase());
                    if (resolvedSymbol) {
                        results.add(resolvedSymbol);
                    }
                }
            }
        }
        return results;
    }
}
