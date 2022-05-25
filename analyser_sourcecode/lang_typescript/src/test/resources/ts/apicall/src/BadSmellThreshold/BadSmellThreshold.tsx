import { updateSystemInfo } from '@/api/addition/systemInfo';
import { BadSmellOption, useBadSmellOption } from "@/api/module/badSmellThresholds";
import useSystemList from '@/store/global-cache-state/useSystemList';
import { storage } from '@/store/storage/sessionStorage';
import { Button, Collapse, Form, notification, Radio } from "antd";
import { useForm } from 'antd/lib/form/Form';
import { Store } from 'antd/lib/form/interface';
import React, { useEffect } from "react";
import styles from "./BadSmellThreshold.less";
import BadSmellThresholdTable from "./components/BadSmellThresholdTable";

const BadSmellThreshold = () => {
    const renderRadio = (badSmellOption: BadSmellOption) => (
        <Radio
            value={badSmellOption.id}
            onClick={(e) => e.stopPropagation()}
        >
            选择
        </Radio>
    );

    const { data } = useBadSmellOption();
    const [form] = useForm();
    const [systemInfoList] = useSystemList();
    const currentSystemId = Number(storage.getSystemId());
    const currentSystemInfo = systemInfoList?.value!.find(systemInfo => systemInfo.id === currentSystemId);
    const originBadSmellThresholdSuiteId = currentSystemInfo?.badSmellThresholdSuiteId;

    const onReset = () => {
        form.setFieldsValue({ badSmellThresholdSuiteId: currentSystemInfo!.badSmellThresholdSuiteId });
    };

    const onChange = (e: any) => {
        form.setFieldsValue({ badSmellThresholdSuiteId: e.target.value });
    }

    const onFinish = (values: Store) => {
        currentSystemInfo!.badSmellThresholdSuiteId = values.badSmellThresholdSuiteId;
        updateSystemInfo(currentSystemInfo).then(() => {
            notification.success({
                type: "success",
                message: "系统信息修改成功！",
            });
        });
    };

    useEffect(() => {
        if (currentSystemInfo) {
            form.setFieldsValue({ badSmellThresholdSuiteId: currentSystemInfo.badSmellThresholdSuiteId });
        }
    });

    return (
        <Form
            form={form}
            onFinish={onFinish}
            initialValues={{ badSmellThresholdSuiteId: originBadSmellThresholdSuiteId }}
        >
            <div className={styles.BadSmellThreshold}>
                <div>请选择合适您系统的指标阈值：</div>
                <Form.Item name="badSmellThresholdSuiteId">
                    <Radio.Group className={styles.badSmellThresholdSuiteId} onChange={onChange}>
                        <Collapse accordion ghost>
                            {data &&
                                data.map((badSmellOption) => {
                                    if (!badSmellOption) return null;
                                    return (
                                        <Collapse.Panel
                                            style={{ marginTop: '10px', border: '1px solid #ddd', borderRadius: '8px' }}
                                            header={badSmellOption.suiteName}
                                            extra={renderRadio(badSmellOption)}
                                            key={badSmellOption.id}
                                        >
                                            <BadSmellThresholdTable data={badSmellOption.thresholds}></BadSmellThresholdTable>
                                        </Collapse.Panel>
                                    );
                                })}
                        </Collapse>
                    </Radio.Group>
                </Form.Item>
            </div>
            <Form.Item className={styles.buttonSuite}>
                <Button htmlType="button" onClick={onReset} className={styles.buttonReset}>
                    重置
                </Button>
                <Button type="primary" htmlType="submit">
                    确定
                </Button>
            </Form.Item>
        </Form>
    );
};

BadSmellThreshold.defaultProps = {};

export default BadSmellThreshold;
