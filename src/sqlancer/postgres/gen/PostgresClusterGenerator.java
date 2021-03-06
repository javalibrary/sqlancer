package sqlancer.postgres.gen;

import java.util.HashSet;
import java.util.Set;

import sqlancer.Query;
import sqlancer.QueryAdapter;
import sqlancer.Randomly;
import sqlancer.postgres.PostgresGlobalState;
import sqlancer.postgres.PostgresSchema.PostgresTable;

public final class PostgresClusterGenerator {

    private PostgresClusterGenerator() {
    }

    public static Query create(PostgresGlobalState globalState) {
        Set<String> errors = new HashSet<>();
        errors.add("there is no previously clustered index for table");
        errors.add("cannot cluster a partitioned table");
        errors.add("access method does not support clustering");
        StringBuilder sb = new StringBuilder("CLUSTER ");
        if (Randomly.getBoolean()) {
            PostgresTable table = globalState.getSchema().getRandomTable(t -> !t.isView());
            sb.append(table.getName());
            if (Randomly.getBoolean() && !table.getIndexes().isEmpty()) {
                sb.append(" USING ");
                sb.append(table.getRandomIndex().getIndexName());
                errors.add("cannot cluster on partial index");
            }
        }
        return new QueryAdapter(sb.toString(), errors);
    }

}
