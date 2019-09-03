package com.wf.ew.core.utils;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis������
   api�ĵ�
 * https://gitee.com/whvse/RedisUtil/blob/master/README.md
 * @author WangFan
 * @date 2018-02-24 ����03:09:50
 * @version 1.1 (GitHub�ĵ�: https://github.com/whvcse/RedisUtil )
 */
public class RedisUtil {
	private StringRedisTemplate redisTemplate;

	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public StringRedisTemplate getRedisTemplate() {
		return this.redisTemplate;
	}

	/** -------------------key��ز���--------------------- */

	/**
	 * ɾ��key
	 * 
	 * @param key
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * ����ɾ��key
	 * 
	 * @param keys
	 */
	public void delete(Collection<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * ���л�key
	 * 
	 * @param key
	 * @return
	 */
	public byte[] dump(String key) {
		return redisTemplate.dump(key);
	}

	/**
	 * �Ƿ����key
	 * 
	 * @param key
	 * @return
	 */
	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * ���ù���ʱ��
	 * 
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public Boolean expire(String key, long timeout, TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	/**
	 * ���ù���ʱ��
	 * 
	 * @param key
	 * @param date
	 * @return
	 */
	public Boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * ����ƥ���key
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * ����ǰ���ݿ�� key �ƶ������������ݿ� db ����
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public Boolean move(String key, int dbIndex) {
		return redisTemplate.move(key, dbIndex);
	}

	/**
	 * �Ƴ� key �Ĺ���ʱ�䣬key ���־ñ���
	 * 
	 * @param key
	 * @return
	 */
	public Boolean persist(String key) {
		return redisTemplate.persist(key);
	}

	/**
	 * ���� key ��ʣ��Ĺ���ʱ��
	 * 
	 * @param key
	 * @param unit
	 * @return
	 */
	public Long getExpire(String key, TimeUnit unit) {
		return redisTemplate.getExpire(key, unit);
	}

	/**
	 * ���� key ��ʣ��Ĺ���ʱ��
	 * 
	 * @param key
	 * @return
	 */
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	/**
	 * �ӵ�ǰ���ݿ����������һ�� key
	 * 
	 * @return
	 */
	public String randomKey() {
		return redisTemplate.randomKey();
	}

	/**
	 * �޸� key ������
	 * 
	 * @param oldKey
	 * @param newKey
	 */
	public void rename(String oldKey, String newKey) {
		redisTemplate.rename(oldKey, newKey);
	}

	/**
	 * ���� newkey ������ʱ���� oldKey ����Ϊ newkey
	 * 
	 * @param oldKey
	 * @param newKey
	 * @return
	 */
	public Boolean renameIfAbsent(String oldKey, String newKey) {
		return redisTemplate.renameIfAbsent(oldKey, newKey);
	}

	/**
	 * ���� key �������ֵ������
	 * 
	 * @param key
	 * @return
	 */
	public DataType type(String key) {
		return redisTemplate.type(key);
	}

	/** -------------------string��ز���--------------------- */

	/**
	 * ����ָ�� key ��ֵ
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * ��ȡָ�� key ��ֵ
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * ���� key ���ַ���ֵ�����ַ�
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String getRange(String key, long start, long end) {
		return redisTemplate.opsForValue().get(key, start, end);
	}

	/**
	 * ������ key ��ֵ��Ϊ value �������� key �ľ�ֵ(old value)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String getAndSet(String key, String value) {
		return redisTemplate.opsForValue().getAndSet(key, value);
	}

	/**
	 * �� key ��������ַ���ֵ����ȡָ��ƫ�����ϵ�λ(bit)
	 * 
	 * @param key
	 * @param offset
	 * @return
	 */
	public Boolean getBit(String key, long offset) {
		return redisTemplate.opsForValue().getBit(key, offset);
	}

	/**
	 * ������ȡ
	 * 
	 * @param keys
	 * @return
	 */
	public List<String> multiGet(Collection<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * ����ASCII��, �ַ���'a'��ASCII����97, תΪ��������'01100001', �˷����ǽ������Ƶ�offsetλֵ��Ϊvalue
	 * 
	 * @param key
	 * @param postion
	 *            λ��
	 * @param value
	 *            ֵ,trueΪ1, falseΪ0
	 * @return
	 */
	public boolean setBit(String key, long offset, boolean value) {
		return redisTemplate.opsForValue().setBit(key, offset, value);
	}

	/**
	 * ��ֵ value ������ key ������ key �Ĺ���ʱ����Ϊ timeout
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 *            ����ʱ��
	 * @param unit
	 *            ʱ�䵥λ, ��:TimeUnit.DAYS Сʱ:TimeUnit.HOURS ����:TimeUnit.MINUTES
	 *            ��:TimeUnit.SECONDS ����:TimeUnit.MILLISECONDS
	 */
	public void setEx(String key, String value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	/**
	 * ֻ���� key ������ʱ���� key ��ֵ
	 * 
	 * @param key
	 * @param value
	 * @return ֮ǰ�Ѿ����ڷ���false,�����ڷ���true
	 */
	public boolean setIfAbsent(String key, String value) {
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	/**
	 * �� value ������д���� key ��������ַ���ֵ����ƫ���� offset ��ʼ
	 * 
	 * @param key
	 * @param value
	 * @param offset
	 *            ��ָ��λ�ÿ�ʼ��д
	 */
	public void setRange(String key, String value, long offset) {
		redisTemplate.opsForValue().set(key, value, offset);
	}

	/**
	 * ��ȡ�ַ����ĳ���
	 * 
	 * @param key
	 * @return
	 */
	public Long size(String key) {
		return redisTemplate.opsForValue().size(key);
	}

	/**
	 * �������
	 * 
	 * @param maps
	 */
	public void multiSet(Map<String, String> maps) {
		redisTemplate.opsForValue().multiSet(maps);
	}

	/**
	 * ͬʱ����һ������ key-value �ԣ����ҽ������и��� key ��������
	 * 
	 * @param maps
	 * @return ֮ǰ�Ѿ����ڷ���false,�����ڷ���true
	 */
	public boolean multiSetIfAbsent(Map<String, String> maps) {
		return redisTemplate.opsForValue().multiSetIfAbsent(maps);
	}

	/**
	 * ����(������), ������Ϊ�Լ�
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long incrBy(String key, long increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Double incrByFloat(String key, double increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * ׷�ӵ�ĩβ
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Integer append(String key, String value) {
		return redisTemplate.opsForValue().append(key, value);
	}

	/** -------------------hash��ز���------------------------- */

	/**
	 * ��ȡ�洢�ڹ�ϣ����ָ���ֶε�ֵ
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public Object hGet(String key, String field) {
		return redisTemplate.opsForHash().get(key, field);
	}

	/**
	 * ��ȡ���и����ֶε�ֵ
	 * 
	 * @param key
	 * @return
	 */
	public Map<Object, Object> hGetAll(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * ��ȡ���и����ֶε�ֵ
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<Object> hMultiGet(String key, Collection<Object> fields) {
		return redisTemplate.opsForHash().multiGet(key, fields);
	}

	public void hPut(String key, String hashKey, String value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	public void hPutAll(String key, Map<String, String> maps) {
		redisTemplate.opsForHash().putAll(key, maps);
	}

	/**
	 * ����hashKey������ʱ������
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 * @return
	 */
	public Boolean hPutIfAbsent(String key, String hashKey, String value) {
		return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
	}

	/**
	 * ɾ��һ��������ϣ���ֶ�
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public Long hDelete(String key, Object... fields) {
		return redisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * �鿴��ϣ�� key �У�ָ�����ֶ��Ƿ����
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hExists(String key, String field) {
		return redisTemplate.opsForHash().hasKey(key, field);
	}

	/**
	 * Ϊ��ϣ�� key �е�ָ���ֶε�����ֵ�������� increment
	 * 
	 * @param key
	 * @param field
	 * @param increment
	 * @return
	 */
	public Long hIncrBy(String key, Object field, long increment) {
		return redisTemplate.opsForHash().increment(key, field, increment);
	}

	/**
	 * Ϊ��ϣ�� key �е�ָ���ֶε�����ֵ�������� increment
	 * 
	 * @param key
	 * @param field
	 * @param delta
	 * @return
	 */
	public Double hIncrByFloat(String key, Object field, double delta) {
		return redisTemplate.opsForHash().increment(key, field, delta);
	}

	/**
	 * ��ȡ���й�ϣ���е��ֶ�
	 * 
	 * @param key
	 * @return
	 */
	public Set<Object> hKeys(String key) {
		return redisTemplate.opsForHash().keys(key);
	}

	/**
	 * ��ȡ��ϣ�����ֶε�����
	 * 
	 * @param key
	 * @return
	 */
	public Long hSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	/**
	 * ��ȡ��ϣ��������ֵ
	 * 
	 * @param key
	 * @return
	 */
	public List<Object> hValues(String key) {
		return redisTemplate.opsForHash().values(key);
	}

	/**
	 * ������ϣ���еļ�ֵ��
	 * 
	 * @param key
	 * @param options
	 * @return
	 */
	public Cursor<Entry<Object, Object>> hScan(String key, ScanOptions options) {
		return redisTemplate.opsForHash().scan(key, options);
	}

	/** ------------------------list��ز���---------------------------- */

	/**
	 * ͨ��������ȡ�б��е�Ԫ��
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String lIndex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * ��ȡ�б�ָ����Χ�ڵ�Ԫ��
	 * 
	 * @param key
	 * @param start
	 *            ��ʼλ��, 0�ǿ�ʼλ��
	 * @param end
	 *            ����λ��, -1��������
	 * @return
	 */
	public List<String> lRange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * �洢��listͷ��
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushAll(String key, String... value) {
		return redisTemplate.opsForList().leftPushAll(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushAll(String key, Collection<String> value) {
		return redisTemplate.opsForList().leftPushAll(key, value);
	}

	/**
	 * ��list���ڵ�ʱ��ż���
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushIfPresent(String key, String value) {
		return redisTemplate.opsForList().leftPushIfPresent(key, value);
	}

	/**
	 * ���pivot����,��pivotǰ�����
	 * 
	 * @param key
	 * @param pivot
	 * @param value
	 * @return
	 */
	public Long lLeftPush(String key, String pivot, String value) {
		return redisTemplate.opsForList().leftPush(key, pivot, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPush(String key, String value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushAll(String key, String... value) {
		return redisTemplate.opsForList().rightPushAll(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushAll(String key, Collection<String> value) {
		return redisTemplate.opsForList().rightPushAll(key, value);
	}

	/**
	 * Ϊ�Ѵ��ڵ��б����ֵ
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushIfPresent(String key, String value) {
		return redisTemplate.opsForList().rightPushIfPresent(key, value);
	}

	/**
	 * ��pivotԪ�ص��ұ����ֵ
	 * 
	 * @param key
	 * @param pivot
	 * @param value
	 * @return
	 */
	public Long lRightPush(String key, String pivot, String value) {
		return redisTemplate.opsForList().rightPush(key, pivot, value);
	}

	/**
	 * ͨ�����������б�Ԫ�ص�ֵ
	 * 
	 * @param key
	 * @param index
	 *            λ��
	 * @param value
	 */
	public void lSet(String key, long index, String value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * �Ƴ�����ȡ�б�ĵ�һ��Ԫ��
	 * 
	 * @param key
	 * @return ɾ����Ԫ��
	 */
	public String lLeftPop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * �Ƴ�����ȡ�б�ĵ�һ��Ԫ�أ� ����б�û��Ԫ�ػ������б�ֱ���ȴ���ʱ���ֿɵ���Ԫ��Ϊֹ
	 * 
	 * @param key
	 * @param timeout
	 *            �ȴ�ʱ��
	 * @param unit
	 *            ʱ�䵥λ
	 * @return
	 */
	public String lBLeftPop(String key, long timeout, TimeUnit unit) {
		return redisTemplate.opsForList().leftPop(key, timeout, unit);
	}

	/**
	 * �Ƴ�����ȡ�б����һ��Ԫ��
	 * 
	 * @param key
	 * @return ɾ����Ԫ��
	 */
	public String lRightPop(String key) {
		return redisTemplate.opsForList().rightPop(key);
	}

	/**
	 * �Ƴ�����ȡ�б�����һ��Ԫ�أ� ����б�û��Ԫ�ػ������б�ֱ���ȴ���ʱ���ֿɵ���Ԫ��Ϊֹ
	 * 
	 * @param key
	 * @param timeout
	 *            �ȴ�ʱ��
	 * @param unit
	 *            ʱ�䵥λ
	 * @return
	 */
	public String lBRightPop(String key, long timeout, TimeUnit unit) {
		return redisTemplate.opsForList().rightPop(key, timeout, unit);
	}

	/**
	 * �Ƴ��б�����һ��Ԫ�أ�������Ԫ����ӵ���һ���б�����
	 * 
	 * @param sourceKey
	 * @param destinationKey
	 * @return
	 */
	public String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
		return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
				destinationKey);
	}

	/**
	 * ���б��е���һ��ֵ����������Ԫ�ز��뵽����һ���б��в��������� ����б�û��Ԫ�ػ������б�ֱ���ȴ���ʱ���ֿɵ���Ԫ��Ϊֹ
	 * 
	 * @param sourceKey
	 * @param destinationKey
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public String lBRightPopAndLeftPush(String sourceKey, String destinationKey,
			long timeout, TimeUnit unit) {
		return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
				destinationKey, timeout, unit);
	}

	/**
	 * ɾ��������ֵ����value��Ԫ��
	 * 
	 * @param key
	 * @param index
	 *            index=0, ɾ������ֵ����value��Ԫ��; index>0, ��ͷ����ʼɾ����һ��ֵ����value��Ԫ��;
	 *            index<0, ��β����ʼɾ����һ��ֵ����value��Ԫ��;
	 * @param value
	 * @return
	 */
	public Long lRemove(String key, long index, String value) {
		return redisTemplate.opsForList().remove(key, index, value);
	}

	/**
	 * �ü�list
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void lTrim(String key, long start, long end) {
		redisTemplate.opsForList().trim(key, start, end);
	}

	/**
	 * ��ȡ�б���
	 * 
	 * @param key
	 * @return
	 */
	public Long lLen(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/** --------------------set��ز���-------------------------- */

	/**
	 * set���Ԫ��
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sAdd(String key, String... values) {
		return redisTemplate.opsForSet().add(key, values);
	}

	/**
	 * set�Ƴ�Ԫ��
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sRemove(String key, Object... values) {
		return redisTemplate.opsForSet().remove(key, values);
	}

	/**
	 * �Ƴ������ؼ��ϵ�һ�����Ԫ��
	 * 
	 * @param key
	 * @return
	 */
	public String sPop(String key) {
		return redisTemplate.opsForSet().pop(key);
	}

	/**
	 * ��Ԫ��value��һ�������Ƶ���һ������
	 * 
	 * @param key
	 * @param value
	 * @param destKey
	 * @return
	 */
	public Boolean sMove(String key, String value, String destKey) {
		return redisTemplate.opsForSet().move(key, value, destKey);
	}

	/**
	 * ��ȡ���ϵĴ�С
	 * 
	 * @param key
	 * @return
	 */
	public Long sSize(String key) {
		return redisTemplate.opsForSet().size(key);
	}

	/**
	 * �жϼ����Ƿ����value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean sIsMember(String key, Object value) {
		return redisTemplate.opsForSet().isMember(key, value);
	}

	/**
	 * ��ȡ�������ϵĽ���
	 * 
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public Set<String> sIntersect(String key, String otherKey) {
		return redisTemplate.opsForSet().intersect(key, otherKey);
	}

	/**
	 * ��ȡkey�����������ϵĽ���
	 * 
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<String> sIntersect(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().intersect(key, otherKeys);
	}

	/**
	 * key������otherKey���ϵĽ����洢��destKey������
	 * 
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long sIntersectAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
				destKey);
	}

	/**
	 * key�����������ϵĽ����洢��destKey������
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long sIntersectAndStore(String key, Collection<String> otherKeys,
			String destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
				destKey);
	}

	/**
	 * ��ȡ�������ϵĲ���
	 * 
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<String> sUnion(String key, String otherKeys) {
		return redisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * ��ȡkey�����������ϵĲ���
	 * 
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<String> sUnion(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * key������otherKey���ϵĲ����洢��destKey��
	 * 
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long sUnionAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 * key�����������ϵĲ����洢��destKey��
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long sUnionAndStore(String key, Collection<String> otherKeys,
			String destKey) {
		return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * ��ȡ�������ϵĲ
	 * 
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public Set<String> sDifference(String key, String otherKey) {
		return redisTemplate.opsForSet().difference(key, otherKey);
	}

	/**
	 * ��ȡkey�����������ϵĲ
	 * 
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public Set<String> sDifference(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().difference(key, otherKeys);
	}

	/**
	 * key������otherKey���ϵĲ�洢��destKey��
	 * 
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long sDifference(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().differenceAndStore(key, otherKey,
				destKey);
	}

	/**
	 * key�����������ϵĲ�洢��destKey��
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long sDifference(String key, Collection<String> otherKeys,
			String destKey) {
		return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
				destKey);
	}

	/**
	 * ��ȡ��������Ԫ��
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Set<String> setMembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	/**
	 * �����ȡ�����е�һ��Ԫ��
	 * 
	 * @param key
	 * @return
	 */
	public String sRandomMember(String key) {
		return redisTemplate.opsForSet().randomMember(key);
	}

	/**
	 * �����ȡ������count��Ԫ��
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public List<String> sRandomMembers(String key, long count) {
		return redisTemplate.opsForSet().randomMembers(key, count);
	}

	/**
	 * �����ȡ������count��Ԫ�ز���ȥ���ظ���
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public Set<String> sDistinctRandomMembers(String key, long count) {
		return redisTemplate.opsForSet().distinctRandomMembers(key, count);
	}

	/**
	 * 
	 * @param key
	 * @param options
	 * @return
	 */
	public Cursor<String> sScan(String key, ScanOptions options) {
		return redisTemplate.opsForSet().scan(key, options);
	}

	/**------------------zSet��ز���--------------------------------*/
	
	/**
	 * ���Ԫ��,���򼯺��ǰ���Ԫ�ص�scoreֵ��С��������
	 * 
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public Boolean zAdd(String key, String value, double score) {
		return redisTemplate.opsForZSet().add(key, value, score);
	}

	/**
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long zAdd(String key, Set<TypedTuple<String>> values) {
		return redisTemplate.opsForZSet().add(key, values);
	}

	/**
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long zRemove(String key, Object... values) {
		return redisTemplate.opsForZSet().remove(key, values);
	}

	/**
	 * ����Ԫ�ص�scoreֵ�����������Ӻ��ֵ
	 * 
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public Double zIncrementScore(String key, String value, double delta) {
		return redisTemplate.opsForZSet().incrementScore(key, value, delta);
	}

	/**
	 * ����Ԫ���ڼ��ϵ�����,���򼯺��ǰ���Ԫ�ص�scoreֵ��С��������
	 * 
	 * @param key
	 * @param value
	 * @return 0��ʾ��һλ
	 */
	public Long zRank(String key, Object value) {
		return redisTemplate.opsForZSet().rank(key, value);
	}

	/**
	 * ����Ԫ���ڼ��ϵ�����,��Ԫ�ص�scoreֵ�ɴ�С����
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long zReverseRank(String key, Object value) {
		return redisTemplate.opsForZSet().reverseRank(key, value);
	}

	/**
	 * ��ȡ���ϵ�Ԫ��, ��С��������
	 * 
	 * @param key
	 * @param start
	 *            ��ʼλ��
	 * @param end
	 *            ����λ��, -1��ѯ����
	 * @return
	 */
	public Set<String> zRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * ��ȡ����Ԫ��, ���Ұ�scoreֵҲ��ȡ
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<TypedTuple<String>> zRangeWithScores(String key, long start,
			long end) {
		return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}

	/**
	 * ����Scoreֵ��ѯ����Ԫ��
	 * 
	 * @param key
	 * @param min
	 *            ��Сֵ
	 * @param max
	 *            ���ֵ
	 * @return
	 */
	public Set<String> zRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	/**
	 * ����Scoreֵ��ѯ����Ԫ��, ��С��������
	 * 
	 * @param key
	 * @param min
	 *            ��Сֵ
	 * @param max
	 *            ���ֵ
	 * @return
	 */
	public Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
			double min, double max) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}

	/**
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
			double min, double max, long start, long end) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
				start, end);
	}

	/**
	 * ��ȡ���ϵ�Ԫ��, �Ӵ�С����
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zReverseRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * ��ȡ���ϵ�Ԫ��, �Ӵ�С����, ������scoreֵ
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<TypedTuple<String>> zReverseRangeWithScores(String key,
			long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, start,
				end);
	}

	/**
	 * ����Scoreֵ��ѯ����Ԫ��, �Ӵ�С����
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> zReverseRangeByScore(String key, double min,
			double max) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}

	/**
	 * ����Scoreֵ��ѯ����Ԫ��, �Ӵ�С����
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<TypedTuple<String>> zReverseRangeByScoreWithScores(
			String key, double min, double max) {
		return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
				min, max);
	}

	/**
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zReverseRangeByScore(String key, double min,
			double max, long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
				start, end);
	}

	/**
	 * ����scoreֵ��ȡ����Ԫ������
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zCount(String key, double min, double max) {
		return redisTemplate.opsForZSet().count(key, min, max);
	}

	/**
	 * ��ȡ���ϴ�С
	 * 
	 * @param key
	 * @return
	 */
	public Long zSize(String key) {
		return redisTemplate.opsForZSet().size(key);
	}

	/**
	 * ��ȡ���ϴ�С
	 * 
	 * @param key
	 * @return
	 */
	public Long zZCard(String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}

	/**
	 * ��ȡ������valueԪ�ص�scoreֵ
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Double zScore(String key, Object value) {
		return redisTemplate.opsForZSet().score(key, value);
	}

	/**
	 * �Ƴ�ָ������λ�õĳ�Ա
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long zRemoveRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().removeRange(key, start, end);
	}

	/**
	 * ����ָ����scoreֵ�ķ�Χ���Ƴ���Ա
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zRemoveRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

	/**
	 * ��ȡkey��otherKey�Ĳ������洢��destKey��
	 * 
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long zUnionAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long zUnionAndStore(String key, Collection<String> otherKeys,
			String destKey) {
		return redisTemplate.opsForZSet()
				.unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * ����
	 * 
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public Long zIntersectAndStore(String key, String otherKey,
			String destKey) {
		return redisTemplate.opsForZSet().intersectAndStore(key, otherKey,
				destKey);
	}

	/**
	 * ����
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Long zIntersectAndStore(String key, Collection<String> otherKeys,
			String destKey) {
		return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys,
				destKey);
	}

	/**
	 * 
	 * @param key
	 * @param options
	 * @return
	 */
	public Cursor<TypedTuple<String>> zScan(String key, ScanOptions options) {
		return redisTemplate.opsForZSet().scan(key, options);
	}
}