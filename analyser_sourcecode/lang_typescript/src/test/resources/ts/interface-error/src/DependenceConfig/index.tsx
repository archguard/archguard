import React, { useState } from "react";
import { useMount } from "react-use";
import _ from "lodash";
import { Card, Drawer, notification } from "antd";
import ConfigForm from "./ConfigForm/index";
import { queryConfig, updateConfig } from '@/api/module/dependenceConfig';
import { ConfigData, configType } from './ConfigForm/config';
import PluginConfig from './PluginConfig';

interface DependenceConfigProps {
  visible: boolean;
  hide: Function;
}

const DependenceConfig = (props: DependenceConfigProps) => {
  const { visible, hide } = props;
  const [configData, setConfigData] = useState({});

  useMount(() => {
    getConfigData();
  });

  const getConfigData = () => {
    queryConfig().then((res) => {
      setConfigData(_.groupBy(res, (item) => item.type));
    });
  };

  const updateConfigData = (data: ConfigData) => {
    const type = Object.keys(data)[0];

    updateConfig(type, data[type])
      .then(() => {
        notification.success({
          message: '保存成功'
        })
      })
  }

  return (
    <Drawer title="配置" placement="right" width="50%" visible={visible} onClose={() => hide()}>
      <PluginConfig data={configData} updateData={updateConfigData}/>
      <div>
        {configType.map((item) => {
          return (
            <Card key={item.type} title={item.label} style={{ marginTop: "20px" }}>
              <ConfigForm
                formItems={item.formItems}
                configType={item.type}
                data={configData}
                updateConfig={updateConfigData}
              />
            </Card>
          );
        })}
      </div>
    </Drawer>
  );
};

export default DependenceConfig;
