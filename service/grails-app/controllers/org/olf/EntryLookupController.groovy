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
        String units = params.units;
        List<DirectoryEntry> result = [];

        Tenants.withId(tenant.toLowerCase()+'_mod_directory', {
            List<Symbol> symbolList = symbolsFromString(symbolsString);
            for (Symbol sym : symbolList) {
                result.add(sym.owner);
                if (units == "yes") {
                    DirectoryEntry parent = sym.owner;
                    for (DirectoryEntry unit : parent.units) {
                        result.add(unit);
                    }
                }
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
                    String authorityString = parts[0];
                    String symbolString = parts[1];
                    List<Symbol> symbol_list = Symbol.executeQuery('select s from Symbol as s where s.authority.symbol = :authority and s.symbol = :symbol',
                            [authority: authorityString?.toUpperCase(), symbol: symbolString?.toUpperCase()]);
                    if (symbol_list.size() == 1) {
                        results.add(symbol_list.get(0));
                    }
                }
            }
        }
        return results;
    }
}
