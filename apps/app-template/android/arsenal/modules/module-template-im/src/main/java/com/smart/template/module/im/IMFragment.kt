package com.smart.template.module.im

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXToastUtil
import kotlinx.android.synthetic.main.im_fragment.*
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently
import org.apache.rocketmq.client.producer.DefaultMQProducer
import org.apache.rocketmq.common.consumer.ConsumeFromWhere
import org.apache.rocketmq.common.message.Message


@Suppress("PrivatePropertyName")
/**
 * https://www.jianshu.com/p/eec1104dcd4f
 * https://www.cnblogs.com/linjiqin/p/7511062.html
 */
class IMFragment : CXBaseFragment() {

    private val TAG = IMFragment::class.java.name

    private val SERVER_ADDRESS_NAME = "http://47.98.184.193:9876"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = arguments ?: Bundle()
        bundle.putString("name", "michael mao")
        callback?.invoke(bundle)
        callback?.invoke(bundle)
        return inflater.inflate(R.layout.im_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSubscribe()

        send_bt?.setOnClickListener {
            val sendMsg = send_et?.text?.toString()
            if (sendMsg.isNullOrBlank()) {
                CXToastUtil.show("请输入需要发送的信息")
                return@setOnClickListener
            } else {

                val producer = DefaultMQProducer("Producer")
                producer.namesrvAddr = SERVER_ADDRESS_NAME
                try {
                    producer.start()
                    val msg = Message("PushTopic", "push", "1", "Just for push1.".toByteArray())

                    val result = producer.send(msg)
                    CXLogUtil.d(TAG, "send id:" + result.getMsgId() + " result:" + result.sendStatus)

                } catch (e: Exception) {
                    CXLogUtil.e(TAG, "send", e)
                } finally {
                    producer.shutdown()
                }
            }
        }

    }

    private fun initSubscribe() {
        val consumer = DefaultMQPushConsumer("PushConsumer")
        consumer.namesrvAddr = SERVER_ADDRESS_NAME
        try {
            //订阅PushTopic下Tag为push的消息
            consumer.subscribe("PushTopic", "push");
            /**
             * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
             * 如果非第一次启动，那么按照上次消费的位置继续消费
             */
            consumer.consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
            consumer.registerMessageListener(
                MessageListenerConcurrently { msgs, _ ->
                    for (msg in msgs) {
                        CXLogUtil.w(TAG, (String(msg.body) + ":" + msg.toString()))
                    }

                    recv_tv?.text = msgs?.joinToString(separator = "\n")

                    ConsumeConcurrentlyStatus.CONSUME_SUCCESS
                }
            )
            consumer.start()
        } catch (e: Exception) {
            CXLogUtil.e(TAG, "recv", e)
        }
    }

}
