package com.grad.project.nc.persistence;

import com.grad.project.nc.model.DataType;
import com.grad.project.nc.model.ProductCharacteristic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


@Repository
public class DataTypeDao extends AbstractDao<DataType> {

    @Autowired
    private ProductCharacteristicDao productCharacteristicDao;

    private DataTypeRowMapper mapper = new DataTypeRowMapper();

    @Autowired
    public DataTypeDao(JdbcTemplate jdbcTemplate){
        super(jdbcTemplate);
    }


    @Transactional
    @Override
    public DataType add(DataType dataType) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO \"data_type\" (data_type)" +
                    " VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, dataType.getDataType());
            return preparedStatement;
        });

        dataType.setDataTypeId(getLongValue(keyHolder, "data_type_id"));
        return find(getLongValue(keyHolder, "data_type_id"));

    }

    @Transactional
    @Override
    public DataType update(DataType dataType) {

        executeUpdate(connection -> {
            String query = "UPDATE data_type SET " +
                    "data_type = ?" +
                    "WHERE data_type_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, dataType.getDataType());
            preparedStatement.setLong(2, dataType.getDataTypeId());

            return preparedStatement;
        });
        return dataType;
    }

    @Transactional
    @Override
    public DataType find(long id) {

        return findOne(connection -> {
            String statement = "SELECT data_type_id, data_type, FROM data_type WHERE data_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, mapper);

    }

    @Transactional
    @Override
    public Collection<DataType> findAll() {
        return findMultiple(connection -> {
            String statement = "SELECT data_type_id" +
                    ",data_type" +
                    "FROM data_type ";
            return connection.prepareStatement(statement);
        }, mapper);

    }

    @Transactional
    @Override
    public void delete(DataType entity) {

        executeUpdate(connection -> {
            String statement = "DELETE FROM data_type WHERE data_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getDataTypeId());
            return preparedStatement;
        });

    }

    private class DataTypeProxy extends DataType{

        public List<ProductCharacteristic> getProductCharacteristics() {

            if (super.getProductCharacteristics() == null){
                super.setProductCharacteristics(new LinkedList<>(productCharacteristicDao.findByProductId(this.getDataTypeId())));
            }

            return super.getProductCharacteristics();
        }
    }

    private static final class DataTypeRowMapper implements RowMapper<DataType> {
        @Override
        public DataType mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataType dataType = new DataType();

            dataType.setDataTypeId(rs.getLong("data_type_id"));
            dataType.setDataType(rs.getString("data_type"));
            return dataType;
        }
    }
}
